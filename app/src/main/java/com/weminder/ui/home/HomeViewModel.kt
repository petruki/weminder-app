package com.weminder.ui.home

import androidx.lifecycle.ViewModel
import com.weminder.data.Group
import com.weminder.utils.MOCK_GROUPS

class HomeViewModel : ViewModel() {

    val mockGroups: List<Group> = MOCK_GROUPS
}