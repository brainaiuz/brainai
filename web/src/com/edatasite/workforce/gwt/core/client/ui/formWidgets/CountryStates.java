/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/7 10:8:39                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.google.gwt.user.client.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Apr 7, 2010
 * Time: 8:46:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class CountryStates {
    final private CommonServiceAsync service = CommonService.App.get();
    private SelectItem[] countries;
    private Map<Integer, List<SelectItem>> states = new HashMap<>();
    private DataListBox country;
    private DataListBox state;
    private Command dataSet;

    public CountryStates(SelectItem[] countries, SelectItem[] states) {
        setCountries(countries);
        setStates(states);
    }

    public CountryStates(final DataListBox country, final DataListBox state) {
        this.country = country;
        this.state = state;
        addListener(this.country);
    }

    //constructor it self gets Country list from database.

    public CountryStates() {
        country = new DataListBox();
        state = new DataListBox();
        addListener(country);
    }

    public void init() {
        service.getCountriesAndRegions(new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
            @Override
            public void failure(Throwable caught) {

            }

            @Override
            public void success(HashMap<String, SelectItem[]> map) {
                setCountries(map.get("country"));
                setStates(map.get("state"));
                if (dataSet != null) {
                    dataSet.execute();
                }
            }
        });
    }

    public void init(boolean isCountry) {
        if (!isCountry) {
            service.getRegions(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem[] regions) {
                    setStates(regions);
                    if (dataSet != null) {
                        dataSet.execute();
                    }
                }
            });
        } else {
            service.getCountries(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem[] countries) {
                    setCountries(countries);
                    if (dataSet != null) {
                        dataSet.execute();
                    }
                }
            });
        }
    }

    public CountryStates(SelectItem[] items, boolean isCountry) {
        if (isCountry) {
            setCountries(items);
            service.getRegions(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem[] regions) {
                    setStates(regions);
                    if (dataSet != null) {
                        dataSet.execute();
                    }
                }
            });
        } else {
            setStates(items);
            service.getCountries(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem[] countries) {
                    setCountries(countries);
                    if (dataSet != null) {
                        dataSet.execute();
                    }
                }
            });
        }
    }

    public void setStates(SelectItem[] result) {
        if (result != null && result.length > 0) {
            for (SelectItem region : result) {
                if (region.getDescription() != null && !"".equals(region.getDescription())) {
                    Integer countryId = null;
                    try {
                        countryId = Integer.valueOf(region.getDescription());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (countryId != null) {
                        if (states.containsKey(countryId)) {
                            states.get(countryId).add(region);
                        } else {
                            List statesList = new ArrayList();
                            statesList.add(region);
                            states.put(countryId, statesList);
                        }
                    }
                }
            }
        }
        if (country != null && country.getSelectedItem() != null) {
            checkForStates(country, state);
        }
    }

    public SelectItem[] getStates(DataListBox countryListBox) {
        if (countryListBox != null && countryListBox.getSelectedItem() != null) {
            return getStates(countryListBox.getSelectedItem());
        } else {
            return null;
        }
    }

    public SelectItem[] getStates(SelectItem country) {
        if (country == null || country.getId() == null || "".equals(country.getId())) {
            return null;
        }
        return getStates(country.getId());
    }

    public SelectItem[] getStates(Integer countryID) {
        if (countryID == null || "".equals(countryID)) {
            return null;
        }
        return states.get(countryID) != null && states.get(countryID).size() > 0 ? states.get(countryID).toArray(new SelectItem[]{}) : null;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public boolean checkForStates(final DataListBox country, final DataListBox state) {
        if (country != null && state != null) {
            if (country.getSelectedItem() != null) {
                return checkForStates(country.getSelectedId(), state);
            }
        }
        state.setSelectedNullLabel();
        state.setEnabled(false);
        return false;
    }

    public boolean checkForStates(Integer countryID, final DataListBox state) {
        if (countryID != null) {
            if (states.containsKey(countryID)) {
                if (states.get(countryID) != null) {
                    state.setItems(Utils.sortSelectItemByName(states.get(countryID).toArray(new SelectItem[]{})));
                }
                state.setEnabled(true);
                return true;
            }
        }
        state.setSelectedNullLabel();
        state.setEnabled(false);
        return false;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
        if (this.country == null) {
            this.country = new DataListBox();
        }
        this.country.setItems(countries);

    }

    public DataListBox getCountryField() {
        if (country == null) {
            country = new DataListBox();
        }
        addListener(country);
        return country;
    }

    public void setCountryField(DataListBox dataListBox) {
        country = dataListBox;
        addListener(country);
    }

    private void addListener(final DataListBox dataListBox) {
        if (dataListBox != null) {
            dataListBox.addValueChangeHandler(event -> checkForStates(dataListBox, state));
        }
    }

    public void setStateField(DataListBox dataListBox) {
        state = dataListBox;
    }

    public DataListBox getStateField() {
        if (state == null) {
            state = new DataListBox();
        }
        return state;
    }

    public void setCountriesStates(SelectItem[] countrys, SelectItem[] states) {
        setCountries(countrys);
        setStates(states);
    }

    public Command getDataSet() {
        return dataSet;
    }

    public void onDataSet(Command dataSet) {
        this.dataSet = dataSet;
    }

    public boolean hasStates() {
        return this.getStates(country) != null;
    }
}
