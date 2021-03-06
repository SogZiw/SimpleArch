package com.song.sakura.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.song.sakura.R
import com.song.sakura.aop.SingleClick
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : IBaseFragment<IBaseViewModel>(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImmersionBar()
        mToolbar?.apply {
            title = "我的"
            navigationIcon = null
        }

        btnAop.setOnClickListener(this)
        btnDialog.setOnClickListener(this)
        btnFlow.setOnClickListener(this)
        btnMotion.setOnClickListener(this)
        btnHomePage.setOnClickListener(this)
        btnHomePage2.setOnClickListener(this)
    }

    @SingleClick
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAop -> {
                val intent = Intent(baseActivity, AopActivity::class.java)
                startActivity(intent)
            }
            R.id.btnDialog -> {
                val intent = Intent(baseActivity, DialogActivity::class.java)
                startActivity(intent)
                baseActivity.overridePendingTransition(R.anim.down_in,R.anim.top_out)
            }
            R.id.btnFlow -> {
                val intent = Intent(baseActivity, FlowActivity::class.java)
                startActivity(intent)
            }
            R.id.btnMotion -> {
                val intent = Intent(baseActivity, MotionLayoutActivity::class.java)
                startActivity(intent)
            }
            R.id.btnHomePage -> {
                val intent = Intent(baseActivity, HomePageActivity::class.java)
                startActivity(intent)
            }

            R.id.btnHomePage2 -> {
                val intent = Intent(baseActivity, HomePageActivity2::class.java)
                startActivity(intent)
            }
        }
    }

}