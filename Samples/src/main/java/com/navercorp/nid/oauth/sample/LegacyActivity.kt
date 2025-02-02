package com.navercorp.nid.oauth.sample

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.sample.databinding.ActivityLegacyBinding
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.nhn.android.naverlogin.OAuthLogin
/**
 *
 * Created on 2021.10.19
 * Updated on 2021.10.19
 *
 * @author Namhoon Kim. (namhun.kim@navercorp.com)
 *         Naver Authentication Platform Development.
 *
 * OAuthLogin의 호환성을 보장하고 테스트하기 위한 클래스
 */
class LegacyActivity : AppCompatActivity() {
    private val TAG = "LegacyActivity"

    private lateinit var binding: ActivityLegacyBinding
    private lateinit var context: Context

    private var clientId = "jyvqXeaVOVmV"
    private var clientSecret = "527300A0_COq1_XV33cf"
    private var clientName = "네이버 아이디로 로그인"

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when(result.resultCode) {
            RESULT_OK -> {
                updateView()
            }
            RESULT_CANCELED -> {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(context, "errorCode:$errorCode, errorDesc:$errorDescription", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = ActivityLegacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        context = this
        // Initialize NAVER id login SDK
        OAuthLogin.getInstance().apply {
            showDevelopersLog(true)
            // Replace with your consumer information.
            init(context, clientId, clientSecret, clientName)
            setMarketLinkWorking(true)
            setShowingBottomTab(true)
        }

        binding.buttonOAuthLoginImg.setOAuthLogin(launcher, object : OAuthLoginCallback {
            override fun onSuccess() {
                updateView()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = OAuthLogin.getInstance().getLastErrorCode(context).code
                val errorDescription = OAuthLogin.getInstance().getLastErrorDesc(context)
                Toast.makeText(
                    context,
                    "errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

        })

        // 로그인
        binding.login.setOnClickListener {
            OAuthLogin.getInstance().initializeLoginFlag()
            OAuthLogin.getInstance().startOauthLoginActivity(this, launcher, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = OAuthLogin.getInstance().getLastErrorCode(context).code
                    val errorDescription = OAuthLogin.getInstance().getLastErrorDesc(context)
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // 로그아웃
        binding.logout.setOnClickListener {
            NaverIdLoginSDK.logout()
            updateView()
        }

        // 연동 끊기
        binding.deleteToken.setOnClickListener {
            NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = OAuthLogin.getInstance().getLastErrorCode(context).code
                    val errorDescription = OAuthLogin.getInstance().getLastErrorDesc(context)
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // 토큰 갱신
        binding.refreshToken.setOnClickListener {
            NidOAuthLogin().callRefreshAccessTokenApi(context, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = OAuthLogin.getInstance().getLastErrorCode(context).code
                    val errorDescription = OAuthLogin.getInstance().getLastErrorDesc(context)
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // Api 호출
        binding.profileApi.setOnClickListener {
            NidOAuthLogin().callProfileApi(object :
                NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {
                    Toast.makeText(
                        context,
                        "$response",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.tvApiResult.text = response.toString()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.tvApiResult.text = ""
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            })
        }

        // 네이버앱 로그인
        binding.loginWithNaverapp.setOnClickListener {
            OAuthLogin.getInstance().enableNaverAppLoginOnly()
            OAuthLogin.getInstance().startOauthLoginActivity(this, launcher, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = OAuthLogin.getInstance().getLastErrorCode(context).code
                    val errorDescription = OAuthLogin.getInstance().getLastErrorDesc(context)
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // 커스텀탭 로그인
        binding.loginWithCustomtabs.setOnClickListener {
            OAuthLogin.getInstance().enableCustomTabLoginOnly()
            OAuthLogin.getInstance().setCustomTabReAuth(false) // 무조건 재인증시 true
            OAuthLogin.getInstance().startOauthLoginActivity(this, launcher, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = OAuthLogin.getInstance().getLastErrorCode(context).code
                    val errorDescription = OAuthLogin.getInstance().getLastErrorDesc(context)
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // 웹뷰 로그인
        binding.loginWithWebView.setOnClickListener {
            OAuthLogin.getInstance().enableWebViewLoginOnly()
            OAuthLogin.getInstance().startOauthLoginActivity(this, launcher, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = OAuthLogin.getInstance().getLastErrorCode(context).code
                    val errorDescription = OAuthLogin.getInstance().getLastErrorDesc(context)
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // ClientSpinner

        val oauthClientSpinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.client_list,
            android.R.layout.simple_spinner_item
        )
        oauthClientSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.consumerListSpinner.prompt = "샘플에서 이용할 client 를 선택하세요"
        binding.consumerListSpinner.adapter = oauthClientSpinnerAdapter
        binding.consumerListSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parents: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                Toast.makeText(this@LegacyActivity,
                    oauthClientSpinnerAdapter.getItem(pos).toString() + "가 선택됨",
                    Toast.LENGTH_SHORT
                ).show()
                if (oauthClientSpinnerAdapter.getItem(pos) == "네이버아이디로로그인") {
                    clientId = "jyvqXeaVOVmV"
                    clientSecret = "527300A0_COq1_XV33cf"
                    clientName = "네이버 아이디로 로그인"
                } else if (oauthClientSpinnerAdapter.getItem(pos) == "소셜게임(12G)") {
                    clientId = "5875kZ1sZ_aL"
                    clientSecret = "509C949A_yi7jOzKU4Pg"
                    clientName = "소셜게임"
                } else if (oauthClientSpinnerAdapter.getItem(pos) == "ERROR_NO_NAME") {
                    clientId= "5875kZ1sZ_aL"
                    clientSecret = "509C949A_yi7jOzKU4Pg"
                    clientName = ""
                } else if (oauthClientSpinnerAdapter.getItem(pos) == "ERROR_CLIENT_ID") {
                    clientId = "5875kZ1sZ_a"
                    clientSecret = "509C949A_yi7jOzKU4Pg"
                    clientName = "ERROR_CLIENT_ID"
                } else if (oauthClientSpinnerAdapter.getItem(pos) == "ERROR_CLIENT_SECRET") {
                    clientId = "jyvqXeaVOVmV"
                    clientSecret = "509C949Ayi7jOzKU4Pg"
                    clientName = "ERROR_CLIENT_SECRET"
                } else {
                    return
                }
                updateUserData()
                NaverIdLoginSDK.initialize(context, clientId, clientSecret, clientName)
            }

            override fun onNothingSelected(parents: AdapterView<*>?) {
                // do nothing
            }

        }

        // Client 정보 변경
        binding.buttonOAuthInit.setOnClickListener {
            clientId = binding.oauthClientid.text.toString()
            clientSecret = binding.oauthClientsecret.text.toString()
            clientName = binding.oauthClientname.text.toString()

            NaverIdLoginSDK.initialize(context, clientId, clientSecret, clientName)

            updateUserData()

        }

        updateUserData()
    }

    private fun updateView() {
        binding.tvAccessToken.text = OAuthLogin.getInstance().getAccessToken(this)
        binding.tvRefreshToken.text = OAuthLogin.getInstance().getRefreshToken(this)
        binding.tvExpires.text = OAuthLogin.getInstance().getExpiresAt(this).toString()
        binding.tvType.text = OAuthLogin.getInstance().getTokenType(this)
        binding.tvState.text = OAuthLogin.getInstance().getState(this).toString()
    }

    private fun updateUserData() {
        binding.oauthClientid.setText(clientId)
        binding.oauthClientsecret.setText(clientSecret)
        binding.oauthClientname.setText(clientName)
    }


}
