package algonquin.cst2335.han00139.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Boolean> isSelected = new MutableLiveData<>();

    public MainViewModel() {
        isSelected.setValue(false);
    }

    public MutableLiveData<Boolean> getIsSelected() {
        return isSelected;
    }
}