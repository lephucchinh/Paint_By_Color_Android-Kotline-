package com.example.paintnumber.ui.daily

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.paintnumber.databinding.FragmentDailyBinding
import com.example.paintnumber.utils.StoryRepository
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.paintnumber.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyFragment : Fragment() {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: StoryRepository
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        repository = StoryRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todayId = getTodayArtworkId()
        val artworks = repository.getArtworks()
        val todayArtwork = artworks.firstOrNull() ?: return

        binding.textTodayTitle.text = todayArtwork.title

        binding.buttonStart.setOnClickListener {
            val ctx = requireContext()
            val lineId = resources.getIdentifier(todayArtwork.lineArtResName, "raw", ctx.packageName)
            val svgId = resources.getIdentifier(todayArtwork.svgResName, "raw", ctx.packageName)
            findNavController().navigate(
                R.id.action_daily_to_sketch_loading,
                bundleOf(
                    "imageId" to todayArtwork.id,
                    "lineArtResId" to lineId,
                    "svgResId" to svgId,
                    "progressPath" to null
                )
            )
        }

        startCountdownToMidnight()
        binding.textStreak.text = "Streak: " + getStreak()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

    private fun getTodayArtworkId(): String {
        val dateStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(System.currentTimeMillis())
        return "daily_$dateStr"
    }

    private fun startCountdownToMidnight() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val millis = cal.timeInMillis - System.currentTimeMillis()
        timer?.cancel()
        timer = object : CountDownTimer(millis, 1000) {
            override fun onTick(ms: Long) {
                val h = ms / (1000 * 60 * 60)
                val m = (ms / (1000 * 60)) % 60
                val s = (ms / 1000) % 60
                binding.textCountdown.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)
            }
            override fun onFinish() { binding.textCountdown.text = "00:00:00" }
        }.start()
    }

    private fun getStreak(): Int {
        val prefs = requireContext().getSharedPreferences("daily", 0)
        return prefs.getInt("streak", 0)
    }
}