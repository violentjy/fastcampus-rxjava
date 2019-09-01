package com.maryang.fastrxjava.ui

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.RecyclerView
import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.data.source.DefaultSingleObserver
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class GithubReposViewModel {
    private val repository = GithubRepository()

    val repos = ObservableArrayList<GithubRepo>()
    val isLoading = ObservableBoolean()

    fun getGithubRepos(): Disposable {
        isLoading.set(true)

        return repository.getGithubRepos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DefaultSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(t: List<GithubRepo>) {
                    isLoading.set(false)

                    repos.clear()
                    repos.addAll(t)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    isLoading.set(false)
                }
            })
    }

    companion object {
        @JvmStatic
        @BindingAdapter("visibility")
        fun setVisibility(view: View, isVisible: Boolean) {
            view.visibility = if (isVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("repos")
        fun setRepos(recyclerView: RecyclerView, repos: ArrayList<GithubRepo>) {
            val adapter = recyclerView.adapter as? GithubReposAdapter ?: return

            adapter.items = repos
        }
    }
}
