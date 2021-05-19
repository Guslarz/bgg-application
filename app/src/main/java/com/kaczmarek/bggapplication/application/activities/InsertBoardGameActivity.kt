package com.kaczmarek.bggapplication.application.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.ArtistAdapter
import com.kaczmarek.bggapplication.application.adapters.BoardGameTypeAdapter
import com.kaczmarek.bggapplication.application.adapters.DefaultValueSpinnerAdapter
import com.kaczmarek.bggapplication.application.adapters.DesignerAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.application.viewmodels.BoardGameDetailsInsertViewModel
import com.kaczmarek.bggapplication.databinding.ActivityInsertBoardGameBinding
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import com.kaczmarek.bggapplication.entities.database.*
import com.squareup.picasso.Picasso
import java.time.LocalDate

class InsertBoardGameActivity : AppCompatActivity() {

    companion object {
        const val PARCEL_NAME = "overview"
        private const val MIN_YEAR_PUBLISHED = 1900
        private const val MAX_YEAR_PUBLISHED = 2100
    }

    private lateinit var binding: ActivityInsertBoardGameBinding
    private val viewModel: BoardGameDetailsInsertViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBoardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinnerType.adapter = BoardGameTypeAdapter(this)

        binding.numberPickerYearPublished.minValue = MIN_YEAR_PUBLISHED
        binding.numberPickerYearPublished.maxValue = MAX_YEAR_PUBLISHED

        binding.recyclerViewArtists.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewArtists.adapter = ArtistAdapter(listOf())

        binding.recyclerViewDesigners.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDesigners.adapter = DesignerAdapter(listOf())

        viewModel.getErrorMessage().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.getAvailableArtists().observe(this, this::onAvailableArtists)
        viewModel.getAvailableDesigners().observe(this, this::onAvailableDesigners)
        viewModel.getAvailableLocations().observe(this, this::onAvailableLocations)

        viewModel.getBoardGame().observe(this, this::onBoardGame)
        viewModel.getArtists().observe(this, this::onArtists)
        viewModel.getDesigners().observe(this, this::onDesigners)
        viewModel.getLocationRelation().observe(this, this::onLocationRelation)

        val overview: BggBoardGameOverview? = intent.getParcelableExtra(PARCEL_NAME)
        viewModel.loadTarget(overview)

        binding.buttonSave.setOnClickListener { onSaveClick() }
        binding.buttonCancel.setOnClickListener { onCancelClick() }
    }

    private fun onAvailableArtists(artists: List<Artist>) {
        binding.spinnerArtists.adapter =
            DefaultValueSpinnerAdapter(this, artists, "-")
    }

    private fun onAvailableDesigners(designers: List<Designer>) {
        binding.spinnerDesigners.adapter =
            DefaultValueSpinnerAdapter(this, designers, "-")
    }

    private fun onAvailableLocations(locations: List<Location>) {
        binding.spinnerLocations.adapter =
            DefaultValueSpinnerAdapter(this, locations, "-")
    }

    private fun onBoardGame(boardGame: BoardGame) {
        setText(binding.editTextTitle, boardGame.title)
        bindCallback(binding.editTextTitle) { boardGame.title = it }

        setText(binding.editTextOriginalTitle, boardGame.originalTitle)
        bindCallback(binding.editTextOriginalTitle) { boardGame.originalTitle = it }

        binding.spinnerType.setSelection(boardGame.type.ordinal, false)
        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?,
                position: Int, id: Long
            ) {
                boardGame.type = parent!!.getItemAtPosition(position) as BoardGameType
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.numberPickerYearPublished.value = boardGame.yearPublished
        binding.numberPickerYearPublished.setOnValueChangedListener { _, _, newVal ->
            boardGame.yearPublished = newVal
        }

        setText(binding.editTextThumbnail, boardGame.thumbnail ?: "")
        bindCallback(binding.editTextThumbnail) {
            boardGame.thumbnail = if (it == "") null else it
            loadThumbnailPreview(it)
        }
        loadThumbnailPreview(boardGame.thumbnail)

        setText(binding.editTextDescription, boardGame.description)
        bindCallback(binding.editTextDescription) { boardGame.description = it }

        setDateInPicker(binding.datePickerOrder, boardGame.orderDate)
        binding.datePickerOrder.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            boardGame.orderDate = LocalDate.of(year, monthOfYear, dayOfMonth)
        }

        setDateInPicker(binding.datePickerAddToCollection, boardGame.addToCollectionDate)
        binding.datePickerAddToCollection.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            boardGame.addToCollectionDate = LocalDate.of(year, monthOfYear, dayOfMonth)
        }

        setText(binding.editTextPrice, boardGame.price.toString())
        bindCallback(binding.editTextPrice) { boardGame.price = it.toDouble() }

        setText(binding.editTextSuggestedRetailPrice, boardGame.suggestedRetailPrice.toString())
        bindCallback(binding.editTextSuggestedRetailPrice) {
            boardGame.suggestedRetailPrice = it.toDouble()
        }

        setText(binding.editTextCodeEanUpc, boardGame.codeEanUpc)
        bindCallback(binding.editTextCodeEanUpc) { boardGame.codeEanUpc = it }

        setText(binding.editTextProductionCode, boardGame.productionCode)
        bindCallback(binding.editTextProductionCode) { boardGame.productionCode = it }

        setText(binding.editTextComment, boardGame.comment)
        bindCallback(binding.editTextComment) { boardGame.comment = it }
    }

    private fun onArtists(artists: List<Artist>) {
        val adapter = ArtistAdapter(artists)
        adapter.setOnDeleteListener(viewModel::removeArtist)
        binding.recyclerViewArtists.adapter = adapter

        binding.buttonAddArtist.setOnClickListener {
            if (binding.spinnerArtists.selectedItemPosition != 0) {
                val artist = binding.spinnerArtists.selectedItem as Artist
                viewModel.addArtist(artist)
            }
        }
    }

    private fun onDesigners(designers: List<Designer>) {
        val adapter = DesignerAdapter(designers)
        adapter.setOnDeleteListener(viewModel::removeDesigner)
        binding.recyclerViewDesigners.adapter = adapter

        binding.buttonAddDesigner.setOnClickListener {
            if (binding.spinnerDesigners.selectedItemPosition != 0) {
                val designer = binding.spinnerDesigners.selectedItem as Designer
                viewModel.addDesigner(designer)
            }
        }
    }

    private fun onLocationRelation(locationRelation: BoardGameLocationRelation) {
        val availableLocations = viewModel.getAvailableLocations().value
        if (availableLocations == null) {
            viewModel.getAvailableLocations().observe(this, {
                onLocationRelation(locationRelation)
            })
        } else {
            val position = availableLocations.indexOfFirst { it.id == locationRelation.locationId }
            binding.spinnerLocations.setSelection(position + 1, false)
            binding.spinnerLocations.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?,
                        position: Int, id: Long
                    ) {
                        val location = parent!!.getItemAtPosition(position)
                        if (location == null) {
                            locationRelation.locationId = null
                        } else {
                            locationRelation.locationId = (location as Location).id
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            setText(binding.editTextLocationComment, locationRelation.comment ?: "")
            bindCallback(binding.editTextLocationComment) {
                locationRelation.comment = if (it == "") null else it
            }
        }
    }

    private fun onSaveClick() {
        currentFocus?.clearFocus()
        viewModel.commit { finish() }
    }

    private fun onCancelClick() {
        finish()
    }

    private fun loadThumbnailPreview(url: String?) {
        if (url != null) {
            Picasso.get()
                .load(url)
                .into(binding.imageViewThumbnailPreview)
        }
    }

    private fun setText(editText: EditText, text: String) {
        editText.setText(text, TextView.BufferType.EDITABLE)
    }

    private fun setDateInPicker(datePicker: DatePicker, date: LocalDate) {
        datePicker.updateDate(date.year, date.monthValue, date.dayOfMonth)
    }

    private fun bindCallback(editText: EditText, callback: (String) -> Unit) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) callback(editText.text.toString())
        }
    }
}