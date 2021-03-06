package com.song.sakura.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.aop.SingleClick
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.ClickAction
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : IBaseFragment<IBaseViewModel>(), ClickAction {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersionBar()
        mToolbar?.apply {
            navigationIcon = null
        }

        setOnClickListener(btn_flash_view)
        setOnClickListener(btn_activity_status)
        setOnClickListener(btn_activity_select_image)
        setOnClickListener(btnVideoPlay)
    }

    @SingleClick
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_flash_view -> {
                startActivity(Intent(getBaseActivity(), WidgetActivity::class.java))
            }
            R.id.btn_activity_status -> {
                startActivity(Intent(getBaseActivity(), LottieActivity::class.java))
            }
            R.id.btn_activity_select_image -> {
                ImageSelectActivity.start(getBaseActivity(), 4, object : OnPhotoSelectListener {
                    override fun onSelected(data: List<String?>?) {
                        ToastUtils.show("选择了$data")
                    }

                    override fun onCancel() {
                        ToastUtils.show("取消了")
                    }
                })
            }
            R.id.btnVideoPlay -> {
                VideoPlayerActivity.start(
                    getBaseActivity(),
                    "http://vfx.mtime.cn/Video/2019/06/29/mp4/190629004821240734.mp4",
                    "速度与激情特别行动"
                )
            }
        }
    }

    override fun <V : View?> findViewById(id: Int): V {
        TODO("Not yet implemented")
    }

}