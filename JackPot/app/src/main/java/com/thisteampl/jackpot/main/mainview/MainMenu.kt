package com.thisteampl.jackpot.main.mainview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.thisteampl.jackpot.R
import com.thisteampl.jackpot.main.MainActivity
import com.thisteampl.jackpot.main.mypage.MyProfile
import kotlinx.android.synthetic.main.activity_main_menu.*


/* Menu */
class MainMenu : Fragment() {


    companion object {

        const val TAG : String = "페이지"

        fun newInstance(): MainMenu {
            return MainMenu()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val popular = arrayListOf(
            Popularity("팀플"),
            Popularity("팀플2"),
            Popularity("팀플3")
        )
//
        //popular_rv.layoutManager = LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false)


//        popular_rv.setHasFixedSize(true)
//
//        popular_rv.adapter = PopularityAdapter(popular)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_myprofile.setOnClickListener{
            activity?.let{
                val intent_myprofile = Intent(context, MyProfile::class.java)
                startActivity(intent_myprofile)
            }
        }

//        btn_project.setOnClickListener{
//            activity?.let{
//                val intent_myproject = Intent(context, SaveProject::class.java)
//                startActivity(intent_myproject)
//            }
//
//        }
//
//        btn_setting.setOnClickListener{
//            activity?.let{
//                val intent_myprofile = Intent(context, MyProfile::class.java)
//                startActivity(intent_myprofile)
//            }
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_main_menu,container,false)
        return view

    }
}