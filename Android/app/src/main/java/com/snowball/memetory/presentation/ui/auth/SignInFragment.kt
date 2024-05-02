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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.snowball.memetory.R
import com.snowball.memetory.databinding.FragmentSignInBinding
import com.snowball.memetory.util.SocialLoginUtil

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!


    private lateinit var socialLoginUtil: SocialLoginUtil
    private var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("SignInFragment", "$result")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            // 로그인 성공, 서버로 로그인 정보 전송
            Toast.makeText(context, "${account.idToken}", Toast.LENGTH_LONG).show()
            Log.d("SocialLoginUtil","email =${account.email}, id=${account.id}\n token =${account.idToken}" +
                    "\nserverAuthCode=${account.serverAuthCode}")
//            sendLoginDataToServer(account.idToken)
//            googleSignOut()
        } catch (e: ApiException) {
            // 로그인 실패 콜백 호출
            Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
            Log.e("SocialLoginUtil", "google login fail = ${e.message}")
        }
//
//        if (result.resultCode == Activity.RESULT_OK) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            Log.d("SignInFragment", "${result.data} // $task")
//            socialLoginUtil.handleGoogleSignInResult(task)
//
//        } else if (result.resultCode == Activity.RESULT_CANCELED) {
//
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            Log.d("SignInFragment", "GoogleSignIn.getSignedInAccountFromIntent(result.data): $task")
//            result.data?.extras?.let { bundle ->
//                for (key in bundle.keySet()) {
//                    Log.d("SignInFragment", "Extra [$key]: ${bundle.get(key)}")
//                }
//            } ?: Log.d("SignInFragment", "Login canceled with no extras")
//        }
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
//            findNavController().navigate(R.id.signUpFragment) // Navigate to another fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}