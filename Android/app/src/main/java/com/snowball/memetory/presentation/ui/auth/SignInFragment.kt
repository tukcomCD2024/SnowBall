package com.snowball.memetory.presentation.ui.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.snowball.memetory.BuildConfig
import com.snowball.memetory.R
import com.snowball.memetory.data.api.AuthService
import com.snowball.memetory.data.api.NetworkModule
import com.snowball.memetory.data.api.NetworkModule.googleAuthService
import com.snowball.memetory.data.dto.auth.request.GoogleTokenRequestDto
import com.snowball.memetory.data.dto.auth.request.SignInRequestDto
import com.snowball.memetory.data.repository.AuthRepository
import com.snowball.memetory.databinding.FragmentSignInBinding
import com.snowball.memetory.util.SocialLoginUtil
import com.snowball.memetory.util.TokenManager

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    private lateinit var socialLoginUtil: SocialLoginUtil
    private var clientSecPwd = BuildConfig.GOOGLE_LOGIN_CLIENT_SECURITY_PASSWORD

    private var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("SignInFragment", "$result")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            // 로그인 성공, 서버로 로그인 정보 전송
            Log.d("SignInFragment","email =${account.email}, id=${account.id}\n token =${account.idToken}" +
                    "\nserverAuthCode=${account.serverAuthCode}")
            authViewModel.requestAccessToken(GoogleTokenRequestDto("authorization_code", BuildConfig.GOOGLE_LOGIN_CLIENT_ID,
                clientSecPwd,"", account.serverAuthCode!!))

//             서버로 엑세스토큰 전송
//            authViewModel.loginUser(SignInRequestDto(account.serverAuthCode!!, "GOOGLE"))

//            sendLoginDataToServer(account.idToken)
//            googleSignOut()
        } catch (e: ApiException) {
            // 로그인 실패 콜백 호출
            Log.e("SignInFragment", "google login fail = ${e.message}")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        TokenManager.init(requireContext())
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authService = NetworkModule.authService
        val googleAuthService = NetworkModule.googleAuthService
        val authRepository = AuthRepository(authService, googleAuthService)

        val factory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        authViewModel.loginResult.observe(viewLifecycleOwner) {
            it.onSuccess {
                Log.d("AUTH", "AccessToken = ${it.accessToken}, RefreshToken = ${it.refreshToken}")
                TokenManager.saveTokens(it.accessToken, it.refreshToken)

                findNavController().navigate(R.id.signUpFragment)

            }
            it.onFailure {
                Log.d("AUTH", "${it.message}")
                Toast.makeText(context, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        // SocialLoginUtil 초기화
        socialLoginUtil = SocialLoginUtil(requireContext(), object : SocialLoginUtil.LoginCallback {
            override fun onLoginFailure(error: Throwable) {
                // 로그인 실패 처리
                // TODO: 사용자에게 실패를 알리는 UI 작업
            }
        })

        binding.googleLoginBtn.setOnClickListener {
            // SocialLoginUtil을 통해 로그인을 시도합니다.
            socialLoginUtil.googleSignOut()
            val signInIntent = socialLoginUtil.getGoogleSignInIntent()
            googleLoginLauncher.launch(signInIntent)
//            socialLoginUtil.loginGoogle(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}