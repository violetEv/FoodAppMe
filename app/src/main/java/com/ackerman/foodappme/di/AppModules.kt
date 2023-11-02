package com.ackerman.foodappme.di

import com.ackerman.foodappme.data.local.database.AppDatabase
import com.ackerman.foodappme.data.local.database.datasource.CartDataSource
import com.ackerman.foodappme.data.local.database.datasource.CartDatabaseDataSource
import com.ackerman.foodappme.data.local.datastore.UserPreferenceDataSource
import com.ackerman.foodappme.data.local.datastore.UserPreferenceDataSourceImpl
import com.ackerman.foodappme.data.local.datastore.appDataStore
import com.ackerman.foodappme.data.network.api.datasource.FoodAppApiDataSource
import com.ackerman.foodappme.data.network.api.datasource.FoodAppDataSource
import com.ackerman.foodappme.data.network.api.service.FoodAppApiService
import com.ackerman.foodappme.data.network.firebase.auth.FirebaseAuthDataSource
import com.ackerman.foodappme.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.ackerman.foodappme.data.repository.CartRepository
import com.ackerman.foodappme.data.repository.CartRepositoryImpl
import com.ackerman.foodappme.data.repository.MenuRepository
import com.ackerman.foodappme.data.repository.MenuRepositoryImpl
import com.ackerman.foodappme.data.repository.UserRepository
import com.ackerman.foodappme.data.repository.UserRepositoryImpl
import com.ackerman.foodappme.presentation.feature.cart.CartViewModel
import com.ackerman.foodappme.presentation.feature.checkout.CheckoutViewModel
import com.ackerman.foodappme.presentation.feature.detail.DetailViewModel
import com.ackerman.foodappme.presentation.feature.editprofile.EditProfileViewModel
import com.ackerman.foodappme.presentation.feature.home.HomeViewModel
import com.ackerman.foodappme.presentation.feature.login.LoginViewModel
import com.ackerman.foodappme.presentation.feature.main.MainViewModel
import com.ackerman.foodappme.presentation.feature.profile.ProfileViewModel
import com.ackerman.foodappme.presentation.feature.register.RegisterViewModel
import com.ackerman.foodappme.presentation.feature.settings.SettingsViewModel
import com.ackerman.foodappme.presentation.feature.splashscreen.SplashScreenViewModel
import com.ackerman.foodappme.utils.PreferenceDataStoreHelper
import com.ackerman.foodappme.utils.PreferenceDataStoreHelperImpl
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
    }
    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { FoodAppApiService.invoke(get()) }
        single { FirebaseAuth.getInstance() }
    }
    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
        single<FoodAppDataSource> { FoodAppApiDataSource(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
    }
    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<MenuRepository> { MenuRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }
    private val viewModelModule = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::CartViewModel)
        viewModelOf(::CheckoutViewModel)
        viewModelOf(::DetailViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::EditProfileViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::SplashScreenViewModel)
        viewModelOf(::SettingsViewModel)
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule
    )
}
