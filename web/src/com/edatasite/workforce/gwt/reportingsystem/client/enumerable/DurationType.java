package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;


import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

/**
 * User: ${Dilsh0d}
 * Date: 19-Mar-2010
 * Time: 21:58:58
 */


public enum DurationType {
    Equals("Equals") {
        public String getStartDate() {
            return null;
        }

        public String getEndDate() {
            return null;
        }
    },
    IsEqualTo("Equals") {
        public String getStartDate() {
            return null;
        }

        public String getEndDate() {
            return null;
        }
    },
    Before("Before") {
        public String getStartDate() {
            return null;
        }

		public String getEndDate() {
			return null;
		}
	},
	Between("Between") {
		public String getStartDate() {
			return null;
		}

		public String getEndDate() {
			return null;
		}
	},
	After("After") {
		public String getStartDate() {
			return null;
		}

		public String getEndDate() {
			return null;
		}
	},
	/*By Week*/
	Before3Week("Before 3 Weeks") {
		public String getStartDate() {
			return getMinusDate(getStartNow(), 3, "week");
		}

		public String getEndDate() {
			return null;
		}
	},
	Before1Week("Before This Week") {
		public String getStartDate() {
			return getStartWeek();
		}

		public String getEndDate() {
			return null;
		}
	},
	BeforeMonth("Before This Month") {
		public String getStartDate() {
			return getStartMonth();
		}

		public String getEndDate() {
			return null;
		}
	},
	Yesterday("Yesterday") {
		public String getStartDate() {
			return getMinusDate(getStartNow(), 1, "day");
		}

		public String getEndDate() {
			return getMinusDate(getEndNow(), 1, "day");
		}
	},
	Today("Today") {
		public String getStartDate() {
			return getStartNow();
		}

		public String getEndDate() {
			return getEndNow();
		}
	},
	Tomorrow("Tomorrow") {
		public String getStartDate() {
			return getPlusDate(getStartNow(), 1, "day");
		}

		public String getEndDate() {
			return getPlusDate(getEndNow(), 1, "day");
		}
	},
	Last3Hour("Last 3 Hours") {
		public String getStartDate() {
			return getMinusDate(getCurrentTimeStamp(), 3, "hour");
		}

		public String getEndDate() {
			return getCurrentTimeStamp();
		}
	},
	Last2Hour("Last 2 Hours") {
		public String getStartDate() {
			return getMinusDate(getCurrentTimeStamp(), 2, "hour");
		}

		public String getEndDate() {
			return getCurrentTimeStamp();
		}
	},
	Last1Hour("Last 1 Hour") {
		public String getStartDate() {
			return getMinusDate(getCurrentTimeStamp(), 1, "hour");
		}

		public String getEndDate() {
			return getCurrentTimeStamp();
		}
	},
	/*By Month*/
	ThisAndLastMonth("This and Last Month") {
		public String getStartDate() {
			return LastMonth.getStartDate();
		}

		public String getEndDate() {
			return ThisMonth.getEndDate();
		}
    },
    LastMonth("Last Month") {
        public String getStartDate() {
            return getMinusDate(getStartMonth(), 1, "month");
        }

        public String getEndDate() {
            return getMinusDate(getStartMonth(), 1, "ms");
        }
    },

    SamePeriodLastYear("SamePeriodLastYear") {
        @Override
        public String getStartDate() {
            return null;
        }

        @Override
        public String getEndDate() {
            return null;
        }
    },
    ThisMonth("This Month") {
        public String getStartDate() {
            return getStartMonth();
        }

        public String getEndDate() {
            return getEndMonth();
        }
    },
    NextMonth("Next Month") {
		public String getStartDate() {
			return getPlusDate(getStartMonth(), 1, "month");
		}

		public String getEndDate() {
			return getMinusDate(getPlusDate(getStartMonth(), 2, "month"), 1, "ms");
		}
	},
	ThisAndNextMonth("This and Next Month") {
		public String getStartDate() {
			return ThisMonth.getStartDate();
		}

		public String getEndDate() {
			return NextMonth.getEndDate();
		}
	},
	/*By quarter*/
	ThisandLastQuarter("This and Last Quarter") {
		public String getStartDate() {
			return LastQuarter.getStartDate();
		}

		public String getEndDate() {
			return ThisQuarter.getEndDate();
		}
	},
	LastQuarter("Last Quarter") {
		public String getStartDate() {
			return getMinusDate(getStartQuarter(), 3, "month");
		}

		public String getEndDate() {
            return getMinusDate(getStartQuarter(), 1, "sec");
        }
	},
	ThisQuarter("This Quarter") {
		public String getStartDate() {
			return getStartQuarter();
		}

		public String getEndDate() {
			return getEndQuarter();
		}
	},
	NextQuarter("Next Quarter") {
		public String getStartDate() {
            return getPlusDate(getEndQuarter(), 1, "sec");
        }

		public String getEndDate() {
            String lastDay = getPlusDate(getEndQuarter(), 1, "sec");
            lastDay = getPlusDate(lastDay, 3, "month");
            lastDay = getMinusDate(lastDay, 1, "sec");
            return lastDay;
        }
	},
	ThisandNextQuarter("This and Next Quarter") {
		public String getStartDate() {
			return ThisQuarter.getStartDate();
		}

		public String getEndDate() {
			return NextQuarter.getEndDate();
		}
	},
	/*By Year*/
	ThisAndLastTwoYears("This and Last Two Years") {
		public String getStartDate() {
			return LastTwoYears.getStartDate();
		}

		public String getEndDate() {
			return ThisYear.getEndDate();
		}
	},
	LastTwoYears("Last Two Years") {
		public String getStartDate() {
			return getMinusDate(getStartYear(), 2, "year");
		}

		public String getEndDate() {
			return getEndYear();
		}
	},
	ThisAndLastYear("This and Last Year") {
		public String getStartDate() {
			return LastYear.getStartDate();
		}

		public String getEndDate() {
			return ThisYear.getEndDate();
		}
	},
	LastYear("Last Year") {
		public String getStartDate() {
			return getMinusDate(getStartYear(), 1, "year");
		}

		public String getEndDate() {
			return getMinusDate(getEndYear(), 1, "year");
		}
	},
	Last3Week("Last 3 Weeks") {
		public String getStartDate() {
			return getMinusDate(getStartNow(), 3, "week");

		}

		public String getEndDate() {
			return getStartNow();
		}
	},
	LastWeek("Last Week") {
		public String getStartDate() {
			return getMinusDate(getStartWeek(), 1, "week");
		}

		public String getEndDate() {
			return getMinusDate(getEndWeek(), 1, "week");
		}
	},
	ThisWeek("This Week") {
		public String getStartDate() {
			return getStartWeek();
		}

		public String getEndDate() {
			return getEndWeek();
		}
	},
	ThisYear("This Year") {
		public String getStartDate() {
			return getStartYear();
		}

		public String getEndDate() {
			return getEndYear();
		}
	},
	NextWeek("Next Week") {
		public String getStartDate() {
			return getPlusDate(getStartWeek(), 1, "week");
		}

		public String getEndDate() {
			return getPlusDate(getEndWeek(), 1, "week");
		}
	},
	NextYear("Next Year") {
		public String getStartDate() {
			return getPlusDate(getStartYear(), 1, "year");
		}

		public String getEndDate() {
			return getPlusDate(getEndYear(), 1, "year");
		}
	},
	ThisAndNextYear("This and Next Year") {
		public String getStartDate() {
			return ThisYear.getStartDate();
		}

		public String getEndDate() {
			return NextYear.getEndDate();
		}
	},


	TwoYearsAgo("Two Years Ago") {
		public String getStartDate() {
			return getMinusDate(getStartYear(), 2, "year");
		}

		public String getEndDate() {
			return getMinusDate(getEndYear(), 2, "year");
		}
	},

	AgeInDays("Age in days") {
		public String getStartDate() {
			return null;
		}

		public String getEndDate() {
			return null;
		}
	};

	public final Date current = new Date();

	DurationType(String name) {
		this.name = name;
	}

	private final String name;

	public String getName() {
		return name;
	}

/*this method get in year and month  month numbers*/

	public int getDaysInMonth(int year, int month) {
		switch (month) {
			case 1:
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
					return 29; // leap year
				} else {
					return 28;
				}
			case 3:
				return 30;
			case 5:
				return 30;
			case 8:
				return 30;
			case 10:
				return 30;
			default:
				return 31;
		}
	}

	public abstract String getStartDate();

	public abstract String getEndDate();

	public String getStartString() {
//        Date date = getStartDate();
//        return date.getYear() + 1900 + "-" + ((date.getMonth() + 1 < 10 ? "0" : "") + (date.getMonth() + 1)) + "-" + ((date.getDate() < 10 ? "0" : "") + date.getDate()) + " 00:00:00";
		return getStartNow();
	}

	public String getEndString() {
//        Date date = getEndDate();
//        return date.getYear() + 1900 + "-" + ((date.getMonth() + 1 < 10 ? "0" : "") + (date.getMonth() + 1)) + "-" + ((date.getDate() < 10 ? "0" : "") + date.getDate()) + " 23:59:59";
		return getEndNow();
	}

	public String getStartNow() {
		return "currentdate()";
	}

	public String getEndNow() {
		return "currentdate() + Interval '1 day - 1 ms'";
	}

	public String getStartWeek() {
		return "currentweekfirstday()";
	}

	public String getEndWeek() {
		return "currentweeklastday() + Interval '1 day - 1 ms'";
	}

	public String getStartMonth() {
		return "currentmonthfirstday()";
	}

	public String getEndMonth() {
		return "currentmonthlastday() + Interval '1 day - 1 ms'";
	}

	public String getStartQuarter() {
		return "currentquarterfirstday()";
	}

	public String getEndQuarter() {
		return "currentquarterlastday() + Interval '1 day - 1 ms'";
	}

	public String getStartYear() {
		return "currentyearfirstday()";
	}

	public String getEndYear() {
		return "currentyearlastday() + Interval '1 day - 1 ms'";
	}

	public String getCurrentTimeStamp() {
		return "currenttimstamp()";
	}

	public String getInterval(Integer n, String w) {
		return "Interval '" + n.toString() + " " + w + "'";
	}

	public String getPlusDate(String current, Integer n, String w) {
		return current + " + " + getInterval(n, w);
	}

	public String getMinusDate(String current, Integer n, String w) {
		return current + " - " + getInterval(n, w);
	}

	public static SelectItem[] asSelectItems() {
		SelectItem[] items = new SelectItem[values().length];
		int i = 0;
        String locale = null;
		for (DurationType type : values()) {
            switch (type.name()) {
                case "Equals":
                    locale = WfmStrings.App.get().equal();
                    break;
                case "Before":
                    locale = WfmStrings.App.get().before();
                    break;
                case "Between":
                    locale = WfmStrings.App.get().between();
                    break;
                case "After":
                    locale = WfmStrings.App.get().after();
                    break;
                case "Before3Week":
                    locale = ReportingStrings.App.get().before3Weeks();
                    break;
                case "Before1Week":
                    locale = ReportingStrings.App.get().beforeThisWeek();
                    break;
                case "BeforeMonth":
                    locale = ReportingStrings.App.get().beforeThisMonth();
                    break;
                case "Yesterday":
                    locale = WfmStrings.App.get().yesterday();
                    break;
                case "Today":
                    locale = WfmStrings.App.get().today();
                    break;
                case "Tomorrow":
                    locale = WfmStrings.App.get().tomorrow();
                    break;
                case "Last3Hour":
                    locale = ReportingStrings.App.get().last3Hours();
                    break;
                case "Last2Hour":
                    locale = ReportingStrings.App.get().last2Hours();
                    break;
                case "Last1Hour":
                    locale = ReportingStrings.App.get().last1Hours();
                    break;
                case "ThisAndLastMonth":
                    locale = ReportingStrings.App.get().thisAndLastMonth();
                    break;
                case "LastMonth":
                    locale = WfmStrings.App.get().lastMonth();
                    break;
                case "ThisMonth":
                    locale = WfmStrings.App.get().thisMonth();
                    break;
                case "NextMonth":
                    locale = WfmStrings.App.get().nextMonth();
                    break;
                case "ThisAndNextMonth":
                    locale = ReportingStrings.App.get().thisAndNextMonth();
                    break;
                case "ThisandLastQuarter":
                    locale = ReportingStrings.App.get().thisAndLastQuarter();
                    break;
                case "LastQuarter":
                    locale = WfmStrings.App.get().lastQuarter();
                    break;
                case "ThisQuarter":
                    locale = WfmStrings.App.get().thisQuarter();
                    break;
                case "NextQuarter":
                    locale = ReportingStrings.App.get().nextQuarter();
                    break;
                case "ThisandNextQuarter":
                    locale = ReportingStrings.App.get().thisAndNextQuarter();
                    break;
                case "ThisAndLastTwoYears":
                    locale = ReportingStrings.App.get().thisAndLastTwoYears();
                    break;
                case "LastTwoYears":
                    locale = ReportingStrings.App.get().last2Years();
                    break;
                case "ThisAndLastYear":
                    locale = ReportingStrings.App.get().thisAndLastYear();
                    break;
                case "LastYear":
                    locale = WfmStrings.App.get().lastYear();
                    break;
                case "Last3Week":
                    locale = ReportingStrings.App.get().last3Weeks();
                    break;
                case "LastWeek":
                    locale = WfmStrings.App.get().lastWeek();
                    break;
                case "ThisWeek":
                    locale = WfmStrings.App.get().thisWeek();
                    break;
                case "ThisYear":
                    locale = WfmStrings.App.get().thisYear();
                    break;
                case "NextWeek":
                    locale = WfmStrings.App.get().nextWeek();
                    break;
                case "NextYear":
                    locale = ReportingStrings.App.get().nextYear();
                    break;
                case "ThisAndNextYear":
                    locale = ReportingStrings.App.get().thisAndNextYear();
                    break;
                case "TwoYearsAgo":
                    locale = ReportingStrings.App.get().twoYearsAgo();
                    break;
                case "AgeInDays":
                    locale = WfmStrings.App.get().ageInDays();
                    break;
                case "SamePeriodLastYear":
                    locale = "Same Period Last Year";
                    break;
            }
            items[i++] = new SelectItem(i, locale, type.name());
		}
		return items;
	}
}
