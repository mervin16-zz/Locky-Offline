package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import com.th3pl4gu3.locky_offline.core.others.User


/*
* Returns the current user
*/
inline val AndroidViewModel.activeUser: User
    get() = getApplication<Application>().activeUser

/*
* Access resources directly fro within
* a view model
*/
inline val AndroidViewModel.resources: Resources
    get() = getApplication<Application>().resources