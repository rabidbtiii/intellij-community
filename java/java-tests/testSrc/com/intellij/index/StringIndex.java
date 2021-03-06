package com.intellij.index;

import com.intellij.openapi.util.Factory;
import com.intellij.util.indexing.DataIndexer;
import com.intellij.util.indexing.IndexStorage;
import com.intellij.util.indexing.MapReduceIndex;
import com.intellij.util.indexing.StorageException;
import com.intellij.util.io.PersistentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eugene Zhuravlev
 *         Date: Dec 12, 2007
 */
public class StringIndex {
  private final MapReduceIndex<String, String, PathContentPair> myIndex;
  
  public StringIndex(final IndexStorage<String, String> storage, final Factory<PersistentHashMap<Integer, Collection<String>>> factory)
    throws IOException {
    myIndex = new MapReduceIndex<String, String, PathContentPair>(null, new Indexer(), storage);
    myIndex.setInputIdToDataKeysIndex(factory);
  }
  
  public List<String> getFilesByWord(String word) throws StorageException {
    return myIndex.getData(word).toValueList();
  }
  
  public void update(final String path, @Nullable String content, @Nullable String oldContent) throws StorageException {
    myIndex.update(path.hashCode(), toInput(path, content), true).compute();
  }
  
  @Nullable 
  private PathContentPair toInput(@NotNull String path, @Nullable String content) {
    return content != null ? new PathContentPair(path, content) : null;
  }

  private static class Indexer implements DataIndexer<String, String, PathContentPair> {
    @Override
    @NotNull
    public Map<String,String> map(final PathContentPair inputData) {
      final Map<String,String> _map = new HashMap<String, String>();
      final StringBuilder builder = new StringBuilder();
      final String content = inputData.content;
      for (int idx = 0; idx < content.length(); idx++) {
        final char ch = content.charAt(idx);
        if (Character.isWhitespace(ch)) {
          if (builder.length() > 0) {
            _map.put(builder.toString(), inputData.path);
            builder.setLength(0);
          }
        }
        else {
          builder.append(ch);
        }
      }
      // emit the last word
      if (builder.length() > 0) {
        _map.put(builder.toString(), inputData.path);
        builder.setLength(0);
      }
      return _map;
    }
  }
  
  private static final class PathContentPair {
    final String path;
    final String content;

    public PathContentPair(final String path, final String content) {
      this.path = path;
      this.content = content;
    }
  }
  
}
