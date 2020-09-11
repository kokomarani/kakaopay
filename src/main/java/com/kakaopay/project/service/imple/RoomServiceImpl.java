package com.kakaopay.project.service.imple;

import com.kakaopay.project.entity.RoomEntity;
import com.kakaopay.project.repository.RoomRepository;
import com.kakaopay.project.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void saveRoom(RoomEntity roomEntity){ roomRepository.save(roomEntity); return;}

}
