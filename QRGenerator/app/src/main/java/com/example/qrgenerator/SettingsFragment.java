package com.example.qrgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.qrgenerator.databinding.FragmentSettingsBinding;
import com.example.qrgenerator.databinding.ItemSettingsMenuBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tombol kembali ke Beranda (Tab Generate) dihapus fungsinya dari toolbar atas
        // karena navigasi utama menggunakan Bottom Navigation.
        binding.btnBackSettings.setVisibility(View.GONE);

        // Setup menu dinamis sesuai bahasa yang aktif
        setupMenu(binding.menuLanguage, R.drawable.ic_language, getString(R.string.title_language), getString(R.string.current_language_name));
        setupMenu(binding.menuAbout, R.drawable.ic_info, getString(R.string.about_app), getString(R.string.version_info));

        binding.menuLanguage.container.setOnClickListener(v -> startActivity(new Intent(getActivity(), LanguageActivity.class)));
        binding.menuAbout.container.setOnClickListener(v -> startActivity(new Intent(getActivity(), AboutActivity.class)));
    }

    private void setupMenu(ItemSettingsMenuBinding itemBinding, int iconRes, String title, String subtitle) {
        itemBinding.ivIcon.setImageResource(iconRes);
        itemBinding.tvTitle.setText(title);
        itemBinding.tvSubtitle.setText(subtitle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
