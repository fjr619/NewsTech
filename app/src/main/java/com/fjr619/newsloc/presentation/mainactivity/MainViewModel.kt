package com.fjr619.newsloc.presentation.mainactivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.newsloc.domain.usecase.appentry.AppEntryUseCases
import com.fjr619.newsloc.presentation.destinations.NewsNavigatorDestination
import com.fjr619.newsloc.presentation.destinations.OnboardingScreenDestination
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
): ViewModel() {

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    private val _startDestination: MutableState<Route> = mutableStateOf(OnboardingScreenDestination)
    val startDestination: State<Route> = _startDestination

    init {
        appEntryUseCases.readAppEntry().onEach { shouldStartFromHomeScreen ->
            if(shouldStartFromHomeScreen){
                _startDestination.value = NewsNavigatorDestination
            }else{
                _startDestination.value = OnboardingScreenDestination
            }
            delay(200) //Without this delay, the onBoarding screen will show for a momentum.
            _splashCondition.value = false
        }.launchIn(viewModelScope)
    }
}