package assistant.stacking.star

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.Toast
import assistant.stacking.star.notifications.fragment_notifications
import kotlinx.android.synthetic.main.fragment_list.*

import kotlinx.android.synthetic.main.fragment_list.view.*
import android.support.v4.os.HandlerCompat.postDelayed
import java.util.*
import kotlin.collections.ArrayList


var parcels:MutableList<String>? = ArrayList()
var showHelp:Boolean=true
var maxCapacity:Int=6
val list = arrayListOf<Int>()
var listInitialized:Boolean=false

class ListFragment : Fragment() {



    // on Nik's the initialisation of parcels is above class

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)
        var ch=ArrayList<View>()
        var  availability ="(Available)"
        parcels?.clear()//
        if (list.isNullOrEmpty()){
            list.addAll(listOf(0,1,2,3,4,5,6))
            listInitialized=true
        }

        for (i in (0..1)){
            for (j in (0..9)){
                if (i==0 &&j==0){
                    continue
                }
                var n:String
                if (i==1) {
                    n = i.toString() + j.toString()
                }else{
                    n=j.toString()
                }


                var ll=view.checkboxes_layout

                var v = ll.getChildAt(10*(i)+j-1)
                if (v is CheckBox){
                    ch.add(v)
                    var checkBoxText="P"+n

                    if (i==0 &&(j<4)){
                        checkBoxText+="    4/4/2019"
                    }
                    else if (i==0 &&j<7){
                        checkBoxText+="    5/4/2019"
                    }

                    else if ( i==0 ){
                        checkBoxText+="    6/4/2019"
                    }
                    else if ( i==1 && j<3){
                        checkBoxText+="    7/4/2019"
                    }
                    else if ( i==1 && j<7){
                        checkBoxText+="    8/4/2019"
                    }
                    else{
                        checkBoxText+="    9/4/2019"
                    }
                    when ((10*i+j).rem(3)){
                        1->{
                            checkBoxText+="    High"
                        }
                        2->{
                            checkBoxText+="    Medium"
                        }
                        0->{
                            checkBoxText+="    Low"
                        }
                    }
                    //demo 3 only
                    if (i==0 && ((j<7))){
                        checkBoxText+=availability
                        //CHANGE COLOR OF UNAVAILABLE PARCELS
                        v.setTextColor(Color.parseColor("#008000"))

                    }
                    else{
                        checkBoxText+="(Unavailable)"
                        v.setTextColor(Color.parseColor("#FF0000"))
                    }
                    v.text=checkBoxText

                }


            }
        }
        view.search_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                var ll=view.checkboxes_layout
                when (s.toString()){
                    ""->{

                        for (i in(0..ch.size-1)){
                            ch.get(i).visibility= VISIBLE
                        }
                        view.button_done.visibility=GONE

                    }
                    else->{
                        for (i in(0..ll.childCount)) {
                            var v =ll.getChildAt(i)
                            if (v is CheckBox){
                                if (v.text.substring(0,s.toString().length).toLowerCase()==s.toString().toLowerCase()){
                                    v.visibility= VISIBLE
                                }else{
                                    v.visibility= GONE
                                }
                            }


                        }
                        view.button_done.visibility= VISIBLE

                    }

                }
            }
        })



        // var confirmButton = (R.id.button_confirm)
        view.button_confirm.setOnClickListener {
            if (parcels.isNullOrEmpty()){
                Toast.makeText(context,"select at least one parcel",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            parcels?.sort()
            // var intent=Intent(this,Reorder::class.java) // old stuff

            val intent = Intent(context, Reorder::class.java)

            startActivity(intent)
        }
        view.button_done.setOnClickListener{
            view.search_text.setText("")
        }


        ch.forEach{

            if (it is CheckBox){


                it.setOnCheckedChangeListener{_,isChecked ->

                    if (isChecked) {
                        if (parcels?.size== maxCapacity) {
                            it.isChecked = false
                            Toast.makeText(context, "max capacity of van is $maxCapacity", Toast.LENGTH_SHORT).show()
                        }
                            else{
                                if ((it.text.toString().contains(availability))) {
                                    var textToAdd=it.text.toString().substring(0,it.text.toString().length-availability.length)
                                    if (parcels?.contains(textToAdd)==false) {

                                        parcels?.add(textToAdd)
                                    }
                                }
                                else{ // parcel unavailable
                                    it.isChecked=false
                                    Toast.makeText(context,"parcel is unavailable for this demo",Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                    else {
                        parcels?.remove(it.text.toString().substring(0,it.text.toString().length-availability.length))

                    }
                }
            }
        }

//


        return view
    }

    override fun onDestroyView() {
        parcels?.clear()
        super.onDestroyView()
    }







}