package cvb.com.br.vendaomie.presentation

sealed class UIStatus<out T : Any> {
    data object Loading : UIStatus<Nothing>()
    data class Error(val error: Throwable) : UIStatus<Nothing>()
    data class Success<T : Any>(val result: T?) : UIStatus<T>()
}
