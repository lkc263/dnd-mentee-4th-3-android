package com.thisteampl.jackpot.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.kakao.sdk.user.UserApiClient
import com.thisteampl.jackpot.R
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.regex.Pattern

/* 회원가입을 위한 화면.
* 지역 스피너 : https://black-jin0427.tistory.com/222 참고했음.
* */

class SignUpActivity : AppCompatActivity() {

    var positionBtn = arrayOfNulls<Button>(3)
    var stateBtn = arrayOfNulls<Button>(3)
    var sGrageBtn = arrayOfNulls<Button>(4)

    var emailCheck: Boolean = false
    var page: Int = 0
    /*
    * page 변수에 따라 보여지는 layout을 visible 해준다.
    * SNS로 시작할 경우 -> 1페이지부터 시작
    * 0 : 이메일, 비밀번호 적는 페이지(이메일로 시작하기)
    * 1 : 닉네임 적는 페이지 / 2 : 지역 / 3 : 직군 / 4 : 상태 / 5 : 스택 / 6 : 자기소개 / 7 : 프로필 공개여부
    * */

    /* 유저 정보에 저장해 둘 3개 SNS의 idx들*/
    var region = "지역" // 지역 저장용
    var position = "직군" // 직군 : 기획자, 개발자, 디자이너
    var state = "상태" // 상태 : 학생, 취업 준비생, 주니어

    //이메일 정규식 확인, https://blog.codejun.space/49
    val EMAIL_ADDRESS_PATTERN : Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupView()
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    //만일 메인화면에서 넘어온 상태로, 회원가입이 된 상태라면 메인화면으로 간다.(기능구현예정)

    // 화면이 구성되고 View를 만들어 준다.
    private fun setupView(){

        var signUpType: Int = intent.getIntExtra("signuptype", 0)
        // 회원가입 타입, 0 : 일반회원가입, 1 : 카카오 로그인, 2 : 네이버 로그인, 3 : 구글 로그인
        var token: String = intent.getStringExtra("token").toString()
        when (signUpType) {

            0 -> {
                signup_email_signup_layout.visibility = View.VISIBLE
            }
            1 -> {
                Toast.makeText(this, "카카오 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                //카카오 로그인을 했을 시 카카오idx와 이름을 불러온다.
                signup_nickname_layout.visibility = View.VISIBLE
                page = 1
                UserApiClient.instance.me { user, error ->
                    signup_name_text.setText(token)
                    //signup_name_text.setText("${user?.kakaoAccount?.profile?.nickname}")
                }

            }
            2 -> {
                Toast.makeText(this, "네이버 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                //네이버 로그인을 했을 시 네이버idx와 이름을 불러온다.
                signup_nickname_layout.visibility = View.VISIBLE
                page = 1
                signup_name_text.setText(token)

            }
            3 -> {
                Toast.makeText(this, "구글 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                //구글 로그인을 했을 시 구글idx와 이름을 불러온다.
                signup_nickname_layout.visibility = View.VISIBLE
                page = 1
                signup_name_text.setText(token)
            }
            // 지역 선택을 해 줄 배열과 액티비티의 스피너와 연결해줄 어댑터.
            // 스피너와 어댑터를 연결
        }

        var regions = listOf("서울","부산","대구","인천","광주",
            "대전","울산","세종","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도")
        // 지역 선택을 해 줄 배열과 액티비티의 스피너와 연결해줄 어댑터.
        signup_region_spinner.setItems(regions) // 스피너와 어댑터를 연결
        signup_region_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            region = newItem
        }

        // 회원가입 다음 버튼
        signup_confirm_button.setOnClickListener {
            if(page == 0) {
                if(!emailCheck){
                    Toast.makeText(this, "이메일 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show()
                }else if(signup_password_text.text.length < 6 || signup_password_text.text.length > 15) {
                    Toast.makeText(this, "비밀번호는 최소 6글자 최대 15글자 입니다.", Toast.LENGTH_SHORT).show()
                } else if(signup_password_check_text.text.toString() != signup_password_text.text.toString()) {
                    Toast.makeText(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    page = 1
                    signup_email_signup_layout.visibility = View.GONE
                    signup_nickname_layout.visibility = View.VISIBLE
                    signup_previous_button.visibility = View.VISIBLE
                }
            } else if(page == 1) {
                if(signup_name_text.text.trim().length < 3 || signup_name_text.text.trim().length > 6) {
                    Toast.makeText(this, "닉네임은 최소 3글자 최대 6글자 입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    page = 2
                    signup_nickname_layout.visibility = View.GONE
                    signup_previous_button.visibility = View.VISIBLE
                    signup_region_layout.visibility = View.VISIBLE
                }
            } else if(page == 2) {
                if(region == "지역") {
                    Toast.makeText(this, "지역을 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    page = 3
                    signup_region_layout.visibility = View.GONE
                    signup_position_layout.visibility = View.VISIBLE
                }
            } else if(page == 3) {
                if(position == "직군") {
                    Toast.makeText(this, "직군을 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    page = 4
                    signup_position_layout.visibility = View.GONE
                    signup_state_layout.visibility = View.VISIBLE
                }
                if(state == "학생") {
                    signup_state_grade_layout.visibility = View.VISIBLE // 학생일때 보이기 (이전갔다가 돌아올 때)
                }
            } else if(page == 4) {
                when (state) {
                    "상태" -> {
                        Toast.makeText(this, "현재 상태를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    "학생" -> {
                        Toast.makeText(this, "학년을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        page = 5
                        signup_state_layout.visibility = View.GONE
                        when (position) {
                            "개발자" -> {
                                signup_developer_stack_layout.visibility = View.VISIBLE
                            }
                            "디자이너" -> {
                                signup_designer_tool_layout.visibility = View.VISIBLE
                            }
                            else -> {
                                page = 6
                                signup_introduce_layout.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            } else if(page == 5) {
                page = 6
                if(position == "개발자") {
                    signup_developer_stack_layout.visibility = View.GONE
                } else {
                    signup_designer_tool_layout.visibility = View.GONE
                }
                signup_introduce_layout.visibility = View.VISIBLE
            } else if(page == 6) {
                page = 7
                signup_introduce_layout.visibility = View.GONE
                signup_open_layout.visibility = View.VISIBLE

                signup_confirm_button.text = "공개할래요"
                signup_previous_button.text = "공개하고싶지 않아요"
            } else if(page == 7) { // 프로필 공개 여부
                Toast.makeText(this, "회원가입이 완료되었습니다.\n프로필을 다른 사람이 확인할 수 있습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        signup_previous_button.setOnClickListener {
            if(page == 1) {
                page = 0
                signup_previous_button.visibility = View.GONE
                signup_email_signup_layout.visibility = View.VISIBLE
                signup_nickname_layout.visibility = View.GONE
            }else if(page == 2) {
                if(signUpType != 0) {
                    signup_previous_button.visibility = View.GONE
                }
                page = 1
                signup_nickname_layout.visibility = View.VISIBLE
                signup_region_layout.visibility = View.GONE
            }else if(page == 3) {
                page = 2
                signup_region_layout.visibility = View.VISIBLE
                signup_position_layout.visibility = View.GONE
            }else if(page == 4) {
                page = 3
                signup_position_layout.visibility = View.VISIBLE
                signup_state_layout.visibility = View.GONE
                if(state == "학생") {
                    signup_state_grade_layout.visibility = View.GONE // 학생일때 가리기
                }
            }else if(page == 5) {
                page = 4
                signup_state_layout.visibility = View.VISIBLE
                if(position == "개발자") {
                    signup_developer_stack_layout.visibility = View.GONE
                } else {
                    signup_designer_tool_layout.visibility = View.GONE
                }
                if(state == "학생") {
                    signup_state_grade_layout.visibility = View.VISIBLE // 학생일때 보이기
                }
            }else if(page == 6) {
                page = 5
                if(position == "개발자") {
                    signup_developer_stack_layout.visibility = View.VISIBLE
                } else {
                    signup_designer_tool_layout.visibility = View.VISIBLE
                }
                signup_introduce_layout.visibility = View.GONE
            }else if(page == 7) { // 프로필 공개 여부.
                Toast.makeText(this, "회원가입이 완료되었습니다.\n프로필을 다른 사람이 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        // 이메일 체크를 위한 메서드, 중복확인하고 이메일을 바꿀 수 있으므로 변경 감지.
        // https://minwook-shin.github.io/android-kotlin-text-watcher/
        signup_id_text.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                //입력이 끝날때 작동
                emailCheck = false
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //입력 전에 작동
                emailCheck = false
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //타이핑 되는 텍스트에 변화가 있을 경우.
                emailCheck = false
            }
        })

        signup_id_check_button.setOnClickListener {
            //아이디 중복확인, 추후에
            if (!checkEmail(signup_id_text.text.toString())) {
                Toast.makeText(this, "올바른 이메일 패턴을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }else {
                emailCheck = true
            }
        }

        signup_exit_button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }

        //직군 선택 리스터
        positionBtn[0] = findViewById(R.id.signup_director_button)
        positionBtn[1] = findViewById(R.id.signup_developer_button)
        positionBtn[2] = findViewById(R.id.signup_designer_button)
        positionBtn[0]?.setOnClickListener { positionBtn[0]?.let { it1 -> this.btnOnClick(it1) } }
        positionBtn[1]?.setOnClickListener {positionBtn[1]?.let { it1 -> this.btnOnClick(it1) } }
        positionBtn[2]?.setOnClickListener { positionBtn[2]?.let { it1 -> this.btnOnClick(it1) }}

        //상태 선택 리스너
        stateBtn[0] = findViewById(R.id.signup_state_student)
        stateBtn[1] = findViewById(R.id.signup_state_jobfinder)
        stateBtn[2] = findViewById(R.id.signup_state_junior)
        stateBtn[0]?.setOnClickListener { stateBtn[0]?.let { it1 -> this.btnOnClick(it1) } }
        stateBtn[1]?.setOnClickListener {stateBtn[1]?.let { it1 -> this.btnOnClick(it1) } }
        stateBtn[2]?.setOnClickListener { stateBtn[2]?.let { it1 -> this.btnOnClick(it1) }}

        //학생 학년 선택 리스너
        stateBtn[0] = findViewById(R.id.signup_gr)
        stateBtn[1] = findViewById(R.id.signup_state_jobfinder)
        stateBtn[2] = findViewById(R.id.signup_state_junior)
        stateBtn[0]?.setOnClickListener { stateBtn[0]?.let { it1 -> this.btnOnClick(it1) } }
        stateBtn[1]?.setOnClickListener {stateBtn[1]?.let { it1 -> this.btnOnClick(it1) } }
        stateBtn[2]?.setOnClickListener { stateBtn[2]?.let { it1 -> this.btnOnClick(it1) }}
    }

    private fun btnOnClick(v : View) {
        var id = v.id
        if(id == R.id.signup_designer_button || id == R.id.signup_developer_button
            || id == R.id.signup_director_button) {
            var pos = Integer.parseInt(v.contentDescription.toString())
            for(i in 0..2) {
                if(i == pos) {
                    positionBtn[i]?.background = ContextCompat.getDrawable(this@SignUpActivity,R.drawable.radius_background_transparent_select)
                    position = positionBtn[i]?.text.toString()
                }
                else {
                    positionBtn[i]?.background = ContextCompat.getDrawable(this@SignUpActivity, R.drawable.radius_background_transparent)
                }
            }
        }

        if(id == R.id.signup_state_student || id == R.id.signup_state_jobfinder
            || id == R.id.signup_state_junior) {
            var pos = Integer.parseInt(v.contentDescription.toString())
            for(i in 0..2) {
                if(i == pos) {
                    stateBtn[i]?.background = ContextCompat.getDrawable(this@SignUpActivity,R.drawable.radius_background_transparent_select)
                    state = stateBtn[i]?.text.toString()
                    if(i == 0) {
                        signup_state_grade_layout.visibility = View.VISIBLE
                    } else {
                        signup_state_grade_layout.visibility = View.GONE
                    }
                }
                else {
                    stateBtn[i]?.background = ContextCompat.getDrawable(this@SignUpActivity, R.drawable.radius_background_transparent)
                }
            }
        }
    }

}