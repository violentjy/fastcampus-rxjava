package com.maryang.fastrxjava.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.data.source.DefaultSingleObserver
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_github_repos.*


class GithubReposActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    private val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

        refreshLayout.setOnRefreshListener { load() }

        load(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    private fun load(showLoading: Boolean = false) {
        if (showLoading)
            showLoading()

        val repoDisposable = viewModel.getGithubRepos()
            .subscribeWith(object: DefaultSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(result: List<GithubRepo>) {
                    hideLoading()
                    adapter.items = result
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    hideLoading()
                }
            })

        disposable.add(repoDisposable)
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }
}
