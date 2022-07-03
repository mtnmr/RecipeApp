package com.example.recipeapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.adapter.CalendarCellAdapter
import com.example.recipeapp.adapter.CalendarItem
import com.example.recipeapp.databinding.FragmentCookingCalendarBinding
import com.example.recipeapp.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import android.view.GestureDetector
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat

@AndroidEntryPoint
class CookingCalendarFragment : Fragment() {

    private var _binding: FragmentCookingCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels()

    private var dateList = listOf<CalendarItem>()

//     タッチイベントを処理するためのインタフェース
    private var mGestureDetector: GestureDetector? = null

    @SuppressLint("ClickableViewAccessibility")
    val listener = View.OnTouchListener { view, motion ->
        mGestureDetector!!.onTouchEvent(motion)
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCookingCalendarBinding.inflate(inflater, container, false)

        mGestureDetector = GestureDetector(requireContext(), mOnGestureListener)

        binding.calenderView.setOnTouchListener(listener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerview

        viewModel.currentDate.observe(viewLifecycleOwner) {
            val thisMonth = SimpleDateFormat("yyyy.MM", Locale.JAPAN).format(it)
            binding.titleText.text = thisMonth
        }

        val adapter = CalendarCellAdapter(dateList) { item, content ->
            val selectDate = SimpleDateFormat("yyyy.MM.dd", Locale.JAPAN).format(item.date)
            val action =
                CookingCalendarFragmentDirections.actionCookingCalendarFragmentToCalendarEditFragment(
                    selectDate = selectDate,
                    mainDish = content
                )
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 7, RecyclerView.VERTICAL, false)


        viewModel.dataList.observe(viewLifecycleOwner) {
            adapter.dateList = it
            adapter.notifyDataSetChanged()
        }

        binding.prevButton.setOnClickListener {
            viewModel.prevMonth()
        }

        binding.nextButton.setOnClickListener {
            viewModel.nextMonth()
        }
    }


    private val MIN_SWIPE_DISTANCE_X = 100
    private val MAX_SWIPE_DISTANCE_X = 2000

    private val mOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            val deltaX: Float
            val deltaY: Float
            if (e1?.x != null && e2?.x != null && e1?.y != null && e2?.y != null) {
                deltaX = e1.x - e2.x
                deltaY = e1.y - e2.y
            } else {
                deltaX = 0.0f
                deltaY = 0.0f
            }
            val deltaXAbs = abs(deltaX)
            val deltaYAbs = abs(deltaY)

            if ((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X)) {
                if (deltaX > 0) {
                    Log.d("Swipe", "Swipe to left")
                    viewModel.nextMonth()

                } else {
                    Log.d("Swipe", "Swipe to right")
                    viewModel.prevMonth()
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}