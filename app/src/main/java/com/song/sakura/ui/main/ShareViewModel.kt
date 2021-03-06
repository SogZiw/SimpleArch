package com.song.sakura.ui.main

import android.app.Application
import com.song.sakura.network.UnPeekLiveData
import com.song.sakura.ui.base.IBaseViewModel

/**
 * Title: com.song.sakura.ui.main
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/03/31 11:00
 *
 * @author SogZiw
 * @version 1.0
 */

class ShareViewModel(app: Application) : IBaseViewModel(app) {

    val closeSlidePanelIfExpanded: UnPeekLiveData<Boolean> = UnPeekLiveData()
}