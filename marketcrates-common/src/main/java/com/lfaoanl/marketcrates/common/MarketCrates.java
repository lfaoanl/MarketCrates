package com.lfaoanl.marketcrates.common;

import com.lfaoanl.marketcrates.Ref;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MarketCrates {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger(Ref.MODID);

    public static IMarketCratesProxy proxy;

}
