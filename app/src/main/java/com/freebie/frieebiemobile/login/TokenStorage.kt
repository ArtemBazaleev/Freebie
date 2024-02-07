package com.freebie.frieebiemobile.login

import com.freebie.frieebiemobile.storage.SecuredKeyValueStorage
import javax.inject.Inject

interface TokenStorage {
    suspend fun getAccessToken(): String?
    suspend fun setToken(token: String)
    suspend fun clearToken()
}

class TokenStorageImpl @Inject constructor(
    private val securedKeyValueStorage: SecuredKeyValueStorage
): TokenStorage {

    override suspend fun getAccessToken(): String? {
        return "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIzMjBkYjAzMC03ZDM3LTRiNjQtODkyOC04Y2Q3MzI0NTMyZGEiLCJzdWIiOiJlOTA1NDJmMTNhOGEzNTA3ZDQzNWQwOWYzNzM0OTQ2MCIsImlzcyI6ImZyZWViaWUuYXV0aCIsImlhdCI6MTcwNjE3NTY2OCwiZXhwIjoxNzA2NzgwNDY4fQ.EjV3A67pyEvydRy8ejH2QvbaTq-rGnBl3nwyN-I_-uyRtOnr2kUOTecPExnH4NSPo_0eNk0wWHFbXys_8hklLnGTrjBDCFgSPs_jZ2g6VTZqiFYh1QmFnZnUM_5qghWq4YK5IWr0BEXyDHAOe_BYFMdynzB6NNxk05Ey8voMGAfpOqwnHKvVdm5n1O1xP3Q-7EB_hK7559Lqzhu0FX1JQulKHGgF5PQon6mjT7ZuK0nVof3S_xUR7TRCgyG6wk0Zh2QWk6HYUQlQiBi1w8pfIgOVaO5YTZogJcg8Lf-xtsbXnxRURP_YdQzEA3nL9bFZ-KZT65wSZ19DBVrNIXx2BQ"//securedKeyValueStorage.getString(KEY, null)
    }

    override suspend fun setToken(token: String) {
        securedKeyValueStorage.putString(KEY, token)
    }

    override suspend fun clearToken() {
        setToken("")
    }

    companion object {
        private const val KEY = "accessTokenKey"
    }
}