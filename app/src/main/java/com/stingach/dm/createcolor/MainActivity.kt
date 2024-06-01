package com.stingach.dm.createcolor

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.SeekBar
import com.stingach.dm.createcolor.databinding.ActivityMainBinding
import com.stingach.dm.createcolor.databinding.ColorBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindings: ColorBinding

    // Добавляем переменные для хранения состояния диалогового окна и цвета
    private var isDialogShown: Boolean = false
    private var currentColor: Int = Color.BLACK
    private var seekBarValues = IntArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Проверяем, было ли сохранено состояние
        if (savedInstanceState != null) {
            // Если да, восстанавливаем сохраненные значения
            isDialogShown = savedInstanceState.getBoolean("isDialogShown", false)
            currentColor = savedInstanceState.getInt("currentColor", Color.BLACK)
            seekBarValues = savedInstanceState.getIntArray("seekBarValues") ?: IntArray(3)
            if (isDialogShown) {
                // Если диалоговое окно было открыто, открываем его снова
                showDialog()
            } else {
                // Если диалоговое окно не было открыто, устанавливаем текущий цвет
                binding.colorBg.setBackgroundColor(currentColor)
            }
        }

        binding.colorbutton.setOnClickListener {
            showDialog()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем состояние диалогового окна и текущий цвет
        outState.putBoolean("isDialogShown", isDialogShown)
        outState.putInt("currentColor", currentColor)
        outState.putIntArray("seekBarValues", seekBarValues)
    }

    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.color, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Choose Color")
        val dialog = dialogBuilder.show()
        bindings = ColorBinding.bind(dialogView)
        isDialogShown = true // Устанавливаем флаг, что диалоговое окно открыто

        // Устанавливаем значения SeekBar'ов, если они были сохранены
        bindings.RR.setText(seekBarValues[0].toString())
        bindings.RED.progress = seekBarValues[0]
        bindings.G.setText(seekBarValues[1].toString())
        bindings.GREEN.progress = seekBarValues[1]
        bindings.B.setText(seekBarValues[2].toString())
        bindings.BLUE.progress = seekBarValues[2]

        bindings.ok.setOnClickListener {
            val red = bindings.RR.text.toString().toIntOrNull() ?: 0
            val green = bindings.G.text.toString().toIntOrNull() ?: 0
            val blue = bindings.B.text.toString().toIntOrNull() ?: 0
            currentColor = Color.rgb(red, green, blue) // Сохраняем выбранный цвет
            binding.colorBg.setBackgroundColor(currentColor) // Устанавливаем цвет фона
            dialog.dismiss() // Не закрываем диалоговое окно
            isDialogShown = false // Устанавливаем флаг, что диалоговое окно закрыто

            // Сбрасываем значения всех SeekBar'ов
            bindings.RED.progress = 0
            bindings.GREEN.progress = 0
            bindings.BLUE.progress = 0
            bindings.RR.setText("0")
            bindings.G.setText("0")
            bindings.B.setText("0")
            seekBarValues = intArrayOf(0, 0, 0) // Обновляем массив значений SeekBar'ов
        }

        bindings.cancel.setOnClickListener {
            dialog.dismiss() // Закрываем диалоговое окно при нажатии на кнопку "CANCEL"
            isDialogShown = false // Устанавливаем флаг, что диалоговое окно закрыто
        }

        // Сохраняем значения SeekBar'ов при их изменении
        bindings.RED.setOnSeekBarChangeListener(createSeekBarChangeListener(bindings.RR, 0))
        bindings.GREEN.setOnSeekBarChangeListener(createSeekBarChangeListener(bindings.G, 1))
        bindings.BLUE.setOnSeekBarChangeListener(createSeekBarChangeListener(bindings.B, 2))
    }

    private fun createSeekBarChangeListener(editText: EditText, index: Int): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                editText.setText(progress.toString())
                seekBarValues[index] = progress // Сохраняем значение SeekBar'а
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }
}