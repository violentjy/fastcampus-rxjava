package com.maryang.fastrxjava.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.databinding.ActivityGithubReposBinding
import io.reactivex.disposables.CompositeDisposable


class GithubReposActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGithubReposBinding

    private val disposable = CompositeDisposable()

    private val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_github_repos)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = this.adapter

        binding.refreshLayout.setOnRefreshListener { load() }

        binding.viewModel = viewModel

        load()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    private fun load() {
        disposable.clear()
        disposable.add(
            viewModel.getGithubRepos()
        )
    }
}
