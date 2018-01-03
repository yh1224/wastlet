package net.assemble.android.mywallet.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.github.salomonbrys.kodein.instance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import net.assemble.android.common.activity.BaseActivity
import net.assemble.android.common.extensions.plusAssign
import net.assemble.android.common.util.RxBus
import net.assemble.android.mywallet.R
import net.assemble.android.mywallet.fragment.ItemEditFragment
import net.assemble.android.mywallet.entity.WalletItem

class ItemEditActivity : BaseActivity() {
    // Instances injected by Kodein
    private val bus: RxBus by instance()

    /** Disposable container for RxJava */
    private lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.item_edit_activity)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        disposables = CompositeDisposable()

        disposables += bus.toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { event ->
                    when (event) {
                        is ItemEditFragment.OnFinished -> {
                            finish()
                        }
                    }
                }

        val itemInfo = intent.getSerializableExtra(EXTRA_ITEM_INFO) as WalletItem?
        val itemEditFragment = if (itemInfo != null) {
            ItemEditFragment.newInstance(itemInfo) // 編集
        } else {
            ItemEditFragment.newInstance() // 新規
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, itemEditFragment)
                .commit()
    }

    companion object {
        @Suppress("unused")
        private val TAG = ItemEditActivity::class.java.simpleName

        const val EXTRA_ITEM_INFO = "itemInfo"
    }
}
