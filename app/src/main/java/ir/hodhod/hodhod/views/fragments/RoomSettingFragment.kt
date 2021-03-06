package ir.hodhod.hodhod.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.data.models.RoomModel
import ir.hodhod.hodhod.databinding.FragmentRoomSettingBinding
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import ir.hodhod.hodhod.viewmodels.ChatViewModel
import ir.hodhod.hodhod.viewmodels.RoomListViewModel
import ir.hodhod.hodhod.views.adapters.UserAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RoomSettingFragment : Fragment(), View.OnClickListener {

    // region of params
    private val brokerSharedViewModel by viewModels<BrokerSharedViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()
    private val roomListViewModel by viewModels<RoomListViewModel>()
    private val args by navArgs<RoomSettingFragmentArgs>()
    private var _binding: FragmentRoomSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var roomKey: String

    // END of region of params

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomKey = args.roomKey
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialView()
        subscribeViews()

        chatViewModel.getUsers(roomKey)
    }

    private fun initialView() {
        binding.roomSettingTitleTextView.text = roomKey

        binding.btnLeave.setOnClickListener(this)
        binding.roomSettingBackImageView.setOnClickListener(this)
    }

    private fun subscribeViews() {
        chatViewModel.getUsersRespond.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvRoomSettingNoUser.visibility = View.VISIBLE
            } else {
                binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
                binding.rvUser.adapter = UserAdapter(it)
                binding.tvRoomSettingNoUser.visibility = View.GONE
            }
        }

        chatViewModel.getUsersError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.problem_occurred, Toast.LENGTH_SHORT).show()
        }

        roomListViewModel.deleteRespond.observe(viewLifecycleOwner) {
            brokerSharedViewModel.unsubscribeFromTopic(roomKey)
            findNavController().navigate(RoomSettingFragmentDirections.actionRoomSettingFragmentToRoomListFragment())
        }

        roomListViewModel.deleteError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.problem_occurred, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLeave -> roomListViewModel.deleteRoom(RoomModel(roomKey))
            binding.roomSettingBackImageView -> findNavController().navigateUp()
        }
    }
}