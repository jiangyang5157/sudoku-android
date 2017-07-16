package com.gmail.jiangyang5157.sudoku.ui

import android.content.pm.PackageManager
import android.widget.TextView
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on July 16, 2017
 */
class AboutFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view?.findViewById(R.id.tv_version) as TextView).text =
                String.format("%s %s",
                        getString(R.string.label_version),
                        activity.packageManager.getPackageInfo(activity.packageName, PackageManager.GET_ACTIVITIES).versionName)
    }

    companion object {
        val TAG = "AboutFragment"
    }
}