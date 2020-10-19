package org.kohsuke.args4j.spi;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeOptionHandler extends OneArgumentOptionHandler<LocalDateTime> {

	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

	public LocalDateTimeOptionHandler(final CmdLineParser parser, final OptionDef option, final Setter<? super LocalDateTime> setter) {
		super(parser, option, setter);
	}

	@Override
	protected LocalDateTime parse(final String argument) throws NumberFormatException, CmdLineException {
		return LocalDateTime.parse(argument, DATE_FORMAT);
	}
}
