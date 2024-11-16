package com.appsv.notesapp.core.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.appsv.notesapp.databinding.ProgressDialogBinding
import com.appsv.notesapp.databinding.SignInDoneBinding


var dialog : AlertDialog? = null


fun Fragment.showSignInWaitDialog(message: String){
    val progress = ProgressDialogBinding.inflate(LayoutInflater.from(requireContext()))
    progress.tvMessage.text = message
    dialog   = AlertDialog.Builder(requireContext()).setView(progress.root).setCancelable(false).create()
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog!!.show()
}


fun hideSignInWaitDialog(){
    dialog?.dismiss()
}

var doneDialog : AlertDialog? = null

fun Fragment.showSignInDoneDialog(){
    val done = SignInDoneBinding.inflate(LayoutInflater.from(requireContext()))
    doneDialog   = AlertDialog.Builder(requireContext()).setView(done.root).setCancelable(false).create()
    doneDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    doneDialog!!.show()
}


fun hideSignInDoneDialog(){
    doneDialog?.dismiss()
}



private var confirmationDialog: AlertDialog? = null

@SuppressLint("UseCompatLoadingForDrawables")
fun Fragment.showConfirmationDialog(
    icon: Int,
    iconTint: Int? = null, // Optional tint color resource
    title: String,
    message: String,
    positiveText: String,
    positiveAction: () -> Unit,
    negativeText: String,
    negativeAction: (() -> Unit)? = null
) {
    val drawable = requireContext().getDrawable(icon)
    iconTint?.let {
        drawable?.setTint(requireContext().getColor(it)) // Apply tint to the drawable
    }

    confirmationDialog = AlertDialog.Builder(requireContext()).apply {
        setIcon(drawable) // Set the tinted drawable as the icon
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveText) { _, _ ->
            positiveAction()
            hideSignInWaitDialog()
        }
        setNegativeButton(negativeText) { _, _ ->
            negativeAction?.invoke()
            hideSignInWaitDialog()
        }
        setCancelable(false)
    }.create()

    confirmationDialog?.show()
}


fun hideConfirmationDialog() {
    confirmationDialog?.dismiss()
    confirmationDialog = null
}

