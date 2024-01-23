package cvb.com.br.vendaomie.di

import android.content.Context
import androidx.room.Room
import cvb.com.br.vendaomie.data.db.AppDataBase
import cvb.com.br.vendaomie.data.db.dao.ItemSaleDao
import cvb.com.br.vendaomie.data.db.dao.SaleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProvide {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            AppDataBase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideSaleDao(appDatabase: AppDataBase): SaleDao {
        return appDatabase.saleDao()
    }

    @Provides
    fun provideItemSaleDao(appDatabase: AppDataBase): ItemSaleDao {
        return appDatabase.itemSaleDao()
    }

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

