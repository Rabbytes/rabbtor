package com.rabbtor.commons.collections;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class CaseInsensitiveHashMap<T> implements Map<String,T>
{
    private Map<CaseInsensitiveMapKey,T> map;
    private KeySet keySet;
    private EntrySet entrySet;
    private Locale locale = Locale.getDefault();

    public CaseInsensitiveHashMap()
    {
        this((Locale)null);
    }

    public CaseInsensitiveHashMap(Locale locale) {
        setLocaleAndMap(locale,createMap());
    }

    protected Map<CaseInsensitiveMapKey, T> createMap()
    {
        return new HashMap<>();
    }

    public CaseInsensitiveHashMap(int initialCapacity) {

        setLocaleAndMap(null,createMap(initialCapacity));
    }


    public CaseInsensitiveHashMap(int initialCapacity, Locale locale) {
        setLocaleAndMap(locale,createMap(initialCapacity));
    }

    protected Map<CaseInsensitiveMapKey, T> createMap(int initialCapacity)
    {
        return new HashMap<>(initialCapacity);
    }

    public CaseInsensitiveHashMap(Map<? extends String, ? extends T> map) {
        this(map.size());
        putAll(map);
    }

    public CaseInsensitiveHashMap(Map<? extends String, ? extends T> map, Locale locale) {
        this(map.size(),locale);
        putAll(map);
    }

    private void setLocaleAndMap(Locale locale, Map<CaseInsensitiveMapKey, T> map)
    {
        if (locale != null)
            this.locale = locale;

        if (map == null)
            this.map = new HashMap<>();
        else
            this.map = map;
    }





    @Override
    public int size()
    {
        return getMap().size();
    }

    @Override
    public boolean isEmpty()
    {
        return getMap().isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return getMap().containsKey(lookupKey(key));
    }

    @Override
    public boolean containsValue(Object value)
    {
        return getMap().containsValue(value);
    }

    @Override
    public T get(Object key)
    {
        return getMap().get(lookupKey(key));
    }

    @Override
    public T put(String key, T value)
    {
        return getMap().put(lookupKey(key), value);
    }

    @Override
    public T remove(Object key)
    {
        return getMap().remove(lookupKey(key));
    }

    /***
     * I completely ignore Java 8 implementation and put one by one.This will be slower.
     */
    @Override
    public void putAll(Map<? extends String, ? extends T> m)
    {
        for (String key : m.keySet()) {
            getMap().put(lookupKey(key),m.get(key));
        }
    }

    @Override
    public void clear()
    {
        getMap().clear();
    }

    @Override
    public Set<String> keySet()
    {
        if (keySet == null)
            keySet = new KeySet(getMap().keySet());
        return keySet;
    }

    @Override
    public Collection<T> values()
    {
        return getMap().values();
    }

    @Override
    public Set<Entry<String, T>> entrySet()
    {
        if (entrySet == null)
            entrySet = new EntrySet(getMap().entrySet());
        return entrySet;
    }

    @Override
    public boolean equals(Object o)
    {
        return getMap().equals(o);
    }

    @Override
    public int hashCode()
    {
        return getMap().hashCode();
    }

    @Override
    public T getOrDefault(Object key, T defaultValue)
    {
        return getMap().getOrDefault(lookupKey(key), defaultValue);
    }

    @Override
    public void forEach(final BiConsumer<? super String, ? super T> action)
    {
        getMap().forEach(new BiConsumer<CaseInsensitiveMapKey, T>()
        {
            @Override
            public void accept(CaseInsensitiveMapKey lookupKey, T t)
            {
                action.accept(lookupKey.key,t);
            }
        });
    }

    @Override
    public void replaceAll(final BiFunction<? super String, ? super T, ? extends T> function)
    {
        getMap().replaceAll(new BiFunction<CaseInsensitiveMapKey, T, T>()
        {
            @Override
            public T apply(CaseInsensitiveMapKey lookupKey, T t)
            {
                return function.apply(lookupKey.key,t);
            }
        });
    }

    @Override
    public T putIfAbsent(String key, T value)
    {
        return getMap().putIfAbsent(lookupKey(key), value);
    }

    @Override
    public boolean remove(Object key, Object value)
    {
        return getMap().remove(lookupKey(key), value);
    }

    @Override
    public boolean replace(String key, T oldValue, T newValue)
    {
        return getMap().replace(lookupKey(key), oldValue, newValue);
    }

    @Override
    public T replace(String key, T value)
    {
        return getMap().replace(lookupKey(key), value);
    }

    @Override
    public T computeIfAbsent(String key, final Function<? super String, ? extends T> mappingFunction)
    {
        return getMap().computeIfAbsent(lookupKey(key), new Function<CaseInsensitiveMapKey, T>()
        {
            @Override
            public T apply(CaseInsensitiveMapKey lookupKey)
            {
                return mappingFunction.apply(lookupKey.key);
            }
        });
    }

    @Override
    public T computeIfPresent(String key, final BiFunction<? super String, ? super T, ? extends T> remappingFunction)
    {
        return getMap().computeIfPresent(lookupKey(key), new BiFunction<CaseInsensitiveMapKey, T, T>()
        {
            @Override
            public T apply(CaseInsensitiveMapKey lookupKey, T t)
            {
                return remappingFunction.apply(lookupKey.key, t);
            }
        });
    }

    @Override
    public T compute(String key, final BiFunction<? super String, ? super T, ? extends T> remappingFunction)
    {
        return getMap().compute(lookupKey(key), new BiFunction<CaseInsensitiveMapKey, T, T>()
        {
            @Override
            public T apply(CaseInsensitiveMapKey lookupKey, T t)
            {
                return remappingFunction.apply(lookupKey.key,t);
            }
        });
    }

    @Override
    public T merge(String key, T value, BiFunction<? super T, ? super T, ? extends T> remappingFunction)
    {
        return getMap().merge(lookupKey(key), value, remappingFunction);
    }


    private Map<CaseInsensitiveMapKey,T> getMap() {
        return map;
    }

    private CaseInsensitiveMapKey lookupKey(Object key)
    {
        return new CaseInsensitiveMapKey((String)key, locale);
    }

    public static class CaseInsensitiveMapKey {
        private String key;
        private String lookupKey;

        public CaseInsensitiveMapKey(String key)
        {
            this(key,null);
        }

        public CaseInsensitiveMapKey(String key, Locale locale)
        {
            this.key = key;
            this.lookupKey = locale == null ? key.toLowerCase() : key.toLowerCase(locale);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CaseInsensitiveMapKey that = (CaseInsensitiveMapKey) o;

            return lookupKey.equals(that.lookupKey);

        }

        @Override
        public int hashCode()
        {
            return lookupKey.hashCode();
        }
    }

    private class KeySet implements Set<String> {

        private Set<CaseInsensitiveMapKey> wrapped;

        public KeySet(Set<CaseInsensitiveMapKey> wrapped)
        {
            this.wrapped = wrapped;
        }


        private List<String> keyList() {
            return stream().collect(Collectors.toList());
        }

        private Collection<CaseInsensitiveMapKey> mapCollection(Collection<?> c) {
            return c.stream().map(it -> lookupKey(it)).collect(Collectors.toList());
        }

        @Override
        public int size()
        {
            return wrapped.size();
        }

        @Override
        public boolean isEmpty()
        {
            return wrapped.isEmpty();
        }

        @Override
        public boolean contains(Object o)
        {
            return wrapped.contains(lookupKey(o));
        }

        @Override
        public Iterator<String> iterator()
        {
            return keyList().iterator();
        }

        @Override
        public Object[] toArray()
        {
            return keyList().toArray();
        }

        @Override
        public <T> T[] toArray(T[] a)
        {
            return keyList().toArray(a);
        }

        @Override
        public boolean add(String s)
        {
            return wrapped.add(lookupKey(s));
        }

        @Override
        public boolean remove(Object o)
        {
            return wrapped.remove(lookupKey(o));
        }

        @Override
        public boolean containsAll(Collection<?> c)
        {
            return keyList().containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends String> c)
        {
            return wrapped.addAll(mapCollection(c));
        }

        @Override
        public boolean retainAll(Collection<?> c)
        {
            return wrapped.retainAll(mapCollection(c));
        }

        @Override
        public boolean removeAll(Collection<?> c)
        {
            return wrapped.removeAll(mapCollection(c));
        }

        @Override
        public void clear()
        {
            wrapped.clear();
        }

        @Override
        public boolean equals(Object o)
        {
            return wrapped.equals(lookupKey(o));
        }

        @Override
        public int hashCode()
        {
            return wrapped.hashCode();
        }

        @Override
        public Spliterator<String> spliterator()
        {
            return keyList().spliterator();
        }

        @Override
        public boolean removeIf(Predicate<? super String> filter)
        {
            return wrapped.removeIf(new Predicate<CaseInsensitiveMapKey>()
            {
                @Override
                public boolean test(CaseInsensitiveMapKey lookupKey)
                {
                    return filter.test(lookupKey.key);
                }
            });
        }

        @Override
        public Stream<String> stream()
        {
            return wrapped.stream().map(it -> it.key);
        }

        @Override
        public Stream<String> parallelStream()
        {
            return wrapped.stream().map(it -> it.key).parallel();
        }

        @Override
        public void forEach(Consumer<? super String> action)
        {
            wrapped.forEach(new Consumer<CaseInsensitiveMapKey>()
            {
                @Override
                public void accept(CaseInsensitiveMapKey lookupKey)
                {
                    action.accept(lookupKey.key);
                }
            });
        }
    }

    private class EntrySet implements Set<Entry<String,T>> {

        private Set<Entry<CaseInsensitiveMapKey,T>> wrapped;

        public EntrySet(Set<Entry<CaseInsensitiveMapKey,T>> wrapped)
        {
            this.wrapped = wrapped;
        }


        private List<Entry<String,T>> keyList() {
            return stream().collect(Collectors.toList());
        }

        private Collection<Entry<CaseInsensitiveMapKey,T>> mapCollection(Collection<?> c) {
            return c.stream().map(it -> new CaseInsensitiveEntryAdapter((Entry<String,T>)it)).collect(Collectors.toList());
        }

        @Override
        public int size()
        {
            return wrapped.size();
        }

        @Override
        public boolean isEmpty()
        {
            return wrapped.isEmpty();
        }

        @Override
        public boolean contains(Object o)
        {
            return wrapped.contains(lookupKey(o));
        }

        @Override
        public Iterator<Entry<String,T>> iterator()
        {
            return keyList().iterator();
        }

        @Override
        public Object[] toArray()
        {
            return keyList().toArray();
        }

        @Override
        public <T> T[] toArray(T[] a)
        {
            return keyList().toArray(a);
        }

        @Override
        public boolean add(Entry<String,T> s)
        {
            return wrapped.add(null );
        }

        @Override
        public boolean remove(Object o)
        {
            return wrapped.remove(lookupKey(o));
        }

        @Override
        public boolean containsAll(Collection<?> c)
        {
            return keyList().containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends Entry<String,T>> c)
        {
            return wrapped.addAll(mapCollection(c));
        }

        @Override
        public boolean retainAll(Collection<?> c)
        {
            return wrapped.retainAll(mapCollection(c));
        }

        @Override
        public boolean removeAll(Collection<?> c)
        {
            return wrapped.removeAll(mapCollection(c));
        }

        @Override
        public void clear()
        {
            wrapped.clear();
        }

        @Override
        public boolean equals(Object o)
        {
            return wrapped.equals(lookupKey(o));
        }

        @Override
        public int hashCode()
        {
            return wrapped.hashCode();
        }

        @Override
        public Spliterator<Entry<String,T>> spliterator()
        {
            return keyList().spliterator();
        }

        @Override
        public boolean removeIf(Predicate<? super Entry<String, T>> filter)
        {
            return wrapped.removeIf(new Predicate<Entry<CaseInsensitiveMapKey, T>>()
            {
                @Override
                public boolean test(Entry<CaseInsensitiveMapKey, T> entry)
                {
                    return filter.test(new FromCaseInsensitiveEntryAdapter(entry));
                }
            });
        }

        @Override
        public Stream<Entry<String,T>> stream()
        {
            return wrapped.stream().map(it -> new Entry<String, T>()
            {
                @Override
                public String getKey()
                {
                    return it.getKey().key;
                }

                @Override
                public T getValue()
                {
                    return it.getValue();
                }

                @Override
                public T setValue(T value)
                {
                    return it.setValue(value);
                }
            });
        }

        @Override
        public Stream<Entry<String,T>> parallelStream()
        {
            return StreamSupport.stream(spliterator(), true);
        }

        @Override
        public void forEach(Consumer<? super Entry<String, T>> action)
        {
            wrapped.forEach(new Consumer<Entry<CaseInsensitiveMapKey, T>>()
            {
                @Override
                public void accept(Entry<CaseInsensitiveMapKey, T> entry)
                {
                    action.accept(new FromCaseInsensitiveEntryAdapter(entry));
                }
            });
        }
    }

    private class EntryAdapter implements Entry<String,T> {
        private Entry<String,T> wrapped;

        public EntryAdapter(Entry<String, T> wrapped)
        {
            this.wrapped = wrapped;
        }

        @Override
        public String getKey()
        {
            return wrapped.getKey();
        }

        @Override
        public T getValue()
        {
            return wrapped.getValue();
        }

        @Override
        public T setValue(T value)
        {
            return wrapped.setValue(value);
        }

        @Override
        public boolean equals(Object o)
        {
            return wrapped.equals(o);
        }

        @Override
        public int hashCode()
        {
            return wrapped.hashCode();
        }


    }

    private class CaseInsensitiveEntryAdapter implements Entry<CaseInsensitiveMapKey,T> {

        private Entry<String,T> wrapped;

        public CaseInsensitiveEntryAdapter(Entry<String, T> wrapped)
        {
            this.wrapped = wrapped;
        }

        @Override
        public CaseInsensitiveMapKey getKey()
        {
            return lookupKey(wrapped.getKey());
        }

        @Override
        public T getValue()
        {
            return wrapped.getValue();
        }

        @Override
        public T setValue(T value)
        {
            return wrapped.setValue(value);
        }
    }

    private class FromCaseInsensitiveEntryAdapter implements Entry<String,T> {

        private Entry<CaseInsensitiveMapKey,T> wrapped;

        public FromCaseInsensitiveEntryAdapter(Entry<CaseInsensitiveMapKey, T> wrapped)
        {
            this.wrapped = wrapped;
        }

        @Override
        public String getKey()
        {
            return wrapped.getKey().key;
        }

        @Override
        public T getValue()
        {
            return wrapped.getValue();
        }

        @Override
        public T setValue(T value)
        {
            return wrapped.setValue(value);
        }
    }


}