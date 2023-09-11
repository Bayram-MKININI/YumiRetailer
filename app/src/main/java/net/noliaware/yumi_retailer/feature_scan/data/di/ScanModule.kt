package net.noliaware.yumi_retailer.feature_scan.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import net.noliaware.yumi_retailer.feature_scan.data.repository.UseVoucherRepositoryImpl
import net.noliaware.yumi_retailer.feature_scan.domain.repository.UseVoucherRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ScanModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindUseVoucherRepository(useVoucherRepository: UseVoucherRepositoryImpl): UseVoucherRepository
}