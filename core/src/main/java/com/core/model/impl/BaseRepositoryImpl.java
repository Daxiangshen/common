package com.core.model.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.model.BaseRepository;
import com.core.model.SuperMapper;

public abstract class BaseRepositoryImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements BaseRepository<T> {
}
