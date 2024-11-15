package com.appsv.notesapp.core.util

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.appsv.notesapp.databinding.ProgressDialogBinding
import com.appsv.notesapp.databinding.SignInDoneBinding


var dialog : AlertDialog? = null


fun Fragment.showDialog(message: String){
    val progress = ProgressDialogBinding.inflate(LayoutInflater.from(requireContext()))
    progress.tvMessage.text = message
    dialog   = AlertDialog.Builder(requireContext()).setView(progress.root).setCancelable(false).create()
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog!!.show()
}


fun hideDialog(){
    dialog?.dismiss()
}

var doneDialog : AlertDialog? = null

fun Fragment.showSignInDoneDialog(){
    val done = SignInDoneBinding.inflate(LayoutInflater.from(requireContext()))
    doneDialog   = AlertDialog.Builder(requireContext()).setView(done.root).setCancelable(false).create()
    doneDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    doneDialog!!.show()
}


fun hidePostDoneDialog(){
    doneDialog?.dismiss()
}



private var confirmationDialog: AlertDialog? = null

fun Fragment.showConfirmationDialog(
    icon: Int,
    title: String,
    message: String,
    positiveText: String,
    positiveAction: () -> Unit,
    negativeText: String,
    negativeAction: (() -> Unit)? = null
) {
    confirmationDialog = AlertDialog.Builder(requireContext()).apply {
        setIcon(icon)
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveText) { _, _ ->
            positiveAction()
            hideDialog()
        }
        setNegativeButton(negativeText) { _, _ ->
            negativeAction?.invoke()
            hideDialog()
        }
        setCancelable(false)
    }.create()

    confirmationDialog?.show()
}

fun hideConfirmationDialog() {
    confirmationDialog?.dismiss()
    confirmationDialog = null
}

