package com.snowball.memetory.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.snowball.memetory.BuildConfig

class SocialLoginUtil (private val context: Context, private val callback: LoginCallback) {

    private var clientId = BuildConfig.GOOGLE_LOGIN_CLIENT_ID

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(clientId)
            .requestScopes(Scope(Scopes.PROFILE)) // 기본 프로필 정보 접근
            .requestIdToken(clientId)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }


    // 아직.
//    private var googleLoginClient: GoogleSignInClient? = null

    private val REQ_GOOGLE_LOGIN = 1001
    interface LoginCallback {
        //        fun onLoginSuccess(memberStatusCheckData : CheckStatus, signUpData : SignUp)
        fun onLoginFailure(error: Throwable)
    }


    fun loginGoogle(activity: Activity) {

//        var signInOptions =
//            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestServerAuthCode(clientId)
//                .requestEmail().build()
//
//        googleLoginClient = GoogleSignIn.getClient(activity, signInOptions)

        googleSignInClient?.let { client ->
            client.signOut().addOnCompleteListener {
                // 로그아웃 후 진행
                activity.startActivityForResult(
                    client.signInIntent,
                    REQ_GOOGLE_LOGIN
                )
            }
        }
    }

    fun getGoogleSignInIntent(): Intent {
        // Google 로그인 인텐트 반환
        Toast.makeText(context, "$googleSignInClient", Toast.LENGTH_LONG).show()
        Log.d("SocialLoginUtil", "getGoogleSignInIntent: $googleSignInClient // ${googleSignInClient.signInIntent} // $clientId")
        return googleSignInClient.signInIntent
    }
    fun googleSignOut() {
        Toast.makeText(context, "로그아웃", Toast.LENGTH_LONG).show()
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 로그아웃이 성공적으로 완료되었을 때의 로직
                Log.d("SocialLoginUtil", "Sign out successful")
            } else {
                // 로그아웃이 실패했을 때의 로직
                Log.e("SocialLoginUtil", "Sign out failed", task.exception)
            }
        }
    }

    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // 로그인 성공, 서버로 로그인 정보 전송
            Toast.makeText(context, "${account.idToken}", Toast.LENGTH_LONG).show()
            Log.d("SocialLoginUtil","email =${account.email}, id=${account.id}, token =${account.idToken}")
//            sendLoginDataToServer(account.idToken)
//            googleSignOut()
        } catch (e: ApiException) {
            // 로그인 실패 콜백 호출
            Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
            Log.e("SocialLoginUtil", "google login fail = ${e.message}")
            callback.onLoginFailure(e)
        }
    }

    private fun sendLoginDataToServer(idToken: String?) {
        // TODO: 서버로 idToken과 로그인 타입 전송 로직 구현

    }

    fun handleGoogleActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity){

        if(requestCode == REQ_GOOGLE_LOGIN){
            // 유저 로그인
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null && account.id != null && account.email != null) {

                    Log.d("SocialLoginUtil","email =${account.email}, id=${account.id}, token =${account.idToken}")
//                    account.email,
//                    account.id,
//                    account.displayName,
//                    account.photoUrl?.path ?: ""

                }

            } catch (e: ApiException) {
                Log.d("SocialLoginUtil", "google login fail = ${e.message}")
                if (e.statusCode == 12501) {
                    // 사용자 취소
                }
            }
        }
    }
}
