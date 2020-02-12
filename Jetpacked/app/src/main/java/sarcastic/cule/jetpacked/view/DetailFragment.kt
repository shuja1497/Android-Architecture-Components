package sarcastic.cule.jetpacked.view


import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

import sarcastic.cule.jetpacked.R
import sarcastic.cule.jetpacked.databinding.FragmentDetailBinding
import sarcastic.cule.jetpacked.databinding.SendSmsDialogBinding
import sarcastic.cule.jetpacked.model.DogBreed
import sarcastic.cule.jetpacked.model.DogPalette
import sarcastic.cule.jetpacked.model.SmsInfo
import sarcastic.cule.jetpacked.utils.getProgressDrawable
import sarcastic.cule.jetpacked.utils.loadImage
import sarcastic.cule.jetpacked.viewmodel.DetailViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var uuid = 0
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var dataBinding: FragmentDetailBinding
    private var smsPermissionStarted = false
    private var currentDog: DogBreed? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            uuid = DetailFragmentArgs.fromBundle(it).uuid
        }

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.getDogBreed(uuid)

        observeDetailedViewModel()

    }

    private fun observeDetailedViewModel() {

        detailViewModel.dogBreed.observe(this, Observer { dogBreed ->

            currentDog = dogBreed

            dogBreed?.let { dog ->
                dataBinding.dog = dog

                dog.imageUrl?.let {
                    setUpBackgroundColor(it)
                }
            }
        })
    }

    private fun setUpBackgroundColor(url: String) {

        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            dataBinding.palette = DogPalette(intColor)
                        }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_send_sms -> {
                smsPermissionStarted = true

                (activity as MainActivity).checkSmsPermission()

            }

            R.id.action_share -> {

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                }

                intent.putExtra(Intent.EXTRA_SUBJECT, "Checkout this dog")
                intent.putExtra(Intent.EXTRA_TEXT, " ${currentDog?.breed} bred for ${currentDog?.bredFor} ")
                intent.putExtra(Intent.EXTRA_STREAM, currentDog?.imageUrl)

                startActivity(Intent.createChooser(intent, "Share with :"))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {

        if (smsPermissionStarted && permissionGranted) {

            context?.let {

                val smsInfo = SmsInfo(
                    "", " ${currentDog?.breed} bred for ${currentDog?.bredFor} ",
                    currentDog?.imageUrl
                )

                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it), R.layout.send_sms_dialog, null, false
                )

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { _, _ ->
                        if (!dialogBinding.smsDestination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                            smsPermissionStarted = false
                        }
                    }
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .show()

                dialogBinding.smsInfo = smsInfo
            }

        }
    }

    private fun sendSms(smsInfo: SmsInfo) {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val smsManager = SmsManager.getDefault()

        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text,
            pendingIntent, null)
    }

}
