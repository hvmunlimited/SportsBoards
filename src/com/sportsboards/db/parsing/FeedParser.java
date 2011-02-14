package com.sportsboards.db.parsing;
import java.io.InputStream;
import java.util.List;

import com.sportsboards.db.Formation;

public interface FeedParser{
	List<Formation> parse(InputStream input);
}