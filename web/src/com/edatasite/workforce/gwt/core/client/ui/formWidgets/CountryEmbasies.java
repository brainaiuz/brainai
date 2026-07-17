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
 * User: Faxriddin  * Date: 01/26/2016
 */
public class CountryEmbasies {
    final private CommonServiceAsync service = CommonService.App.get();
    private SelectItem[] countries;
    private Map<Integer, List<SelectItem>> embasies = new HashMap<>();
    private DataListBox country;
    private DataListBox embassy;
    private Command dataSet;

    public CountryEmbasies(SelectItem[] countries, SelectItem[] states) {
        setCountries(countries);
        setEmbasies(states);
    }

    public CountryEmbasies(final DataListBox country, final DataListBox embassy) {
        this.country = country;
        this.embassy = embassy;
        addListener(this.country);
    }

    //constructor it self gets Country list from database.

    public CountryEmbasies() {
        country = new DataListBox();
        embassy = new DataListBox();
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
                setEmbasies(map.get("embassy"));
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
                    setEmbasies(regions);
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

    public CountryEmbasies(SelectItem[] items, boolean isCountry) {
        if (isCountry) {
            setCountries(items);
            service.getRegions(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem[] regions) {
                    setEmbasies(regions);
                    if (dataSet != null) {
                        dataSet.execute();
                    }
                }
            });
        } else {
            setEmbasies(items);
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

    public void setEmbasies(SelectItem[] result) {
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
                        if (embasies.containsKey(countryId)) {
                            embasies.get(countryId).add(region);
                        } else {
                            List statesList = new ArrayList();
                            statesList.add(region);
                            embasies.put(countryId, statesList);
                        }
                    }
                }
            }
        }
        if (country != null && country.getSelectedItem() != null) {
            checkForStates(country, embassy);
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
        return embasies.get(countryID) != null && embasies.get(countryID).size() > 0 ? embasies.get(countryID).toArray(new SelectItem[]{}) : null;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public boolean checkForStates(final DataListBox country, final DataListBox embassy) {
        if (country != null && embassy != null) {
            if (country.getSelectedItem() != null) {
                return checkForStates(country.getSelectedId(), embassy);
            }
        }
        embassy.setSelectedNullLabel();
        embassy.setEnabled(false);
        return false;
    }

    public boolean checkForStates(Integer countryID, final DataListBox embassy) {
        if (countryID != null) {
            if (embasies.containsKey(countryID)) {
                if (embasies.get(countryID) != null) {
                    embassy.setItems(Utils.sortSelectItemByName(embasies.get(countryID).toArray(new SelectItem[]{})));
                }
                embassy.setEnabled(true);
                return true;
            }
        }
        embassy.setSelectedNullLabel();
        embassy.setEnabled(false);
        return false;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
        if (this.country == null) {
            this.country = new DataListBox();
        }
        this.country.setItems(Utils.sortSelectItemByName(countries));

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
            dataListBox.addValueChangeHandler(event -> checkForStates(dataListBox, embassy));
        }
    }

    public void setStateField(DataListBox dataListBox) {
        embassy = dataListBox;
    }

    public DataListBox getStateField() {
        if (embassy == null) {
            embassy = new DataListBox();
        }
        return embassy;
    }

    public void setCountriesStates(SelectItem[] countrys, SelectItem[] states) {
        setCountries(countrys);
        setEmbasies(states);
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
