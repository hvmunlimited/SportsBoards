package com.sportsboards.db.parsing;

import java.io.InputStream;
import java.util.List;
import com.sportsboards.db.Formation;

/**
 * Coded by Nathan King
 */

public interface FeedParser{
	List<Formation> parse(InputStream input);
}