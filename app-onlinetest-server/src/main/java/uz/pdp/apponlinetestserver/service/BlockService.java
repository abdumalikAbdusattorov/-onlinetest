package uz.pdp.apponlinetestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import uz.pdp.apponlinetestserver.entity.Block;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.exception.ResourceNotFoundException;
import uz.pdp.apponlinetestserver.payload.*;
import uz.pdp.apponlinetestserver.repository.BlockRepository;
import uz.pdp.apponlinetestserver.utils.CommonUtils;

import java.util.stream.Collectors;

@Service
public class BlockService {
    @Autowired
    BlockRepository blockRepository;

    public ApiResponse saveOrEditBlock(ReqBlock reqBlock) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setMessage("Saved");
            apiResponse.setSuccess(true);
            Block block = new Block();
            if (reqBlock.getId() != null) {
                block = blockRepository.findById(reqBlock.getId()).orElseThrow(() -> new ResourceNotFoundException("Block", "id", reqBlock.getId()));
                apiResponse.setMessage("Edited");
            }
            block.setNameUz(reqBlock.getNameUz());
            block.setNameRu(reqBlock.getNameRu());
            block.setLevel(reqBlock.getLevel());
            blockRepository.save(block);

        } catch (Exception e) {
            apiResponse.setMessage("Error");
            apiResponse.setSuccess(false);
        }
        return apiResponse;
    }

    public ApiResponse deletBlock(Integer id) {
        try {
            blockRepository.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    public ResPageable getBlockByPageable(Integer page, Integer size) {
        Page<Block> blockPage = blockRepository.findAll(CommonUtils.getPageableById(page, size));
        return new ResPageable(blockPage.getContent(), blockPage.getTotalElements(), page);
    }

    public ApiResponseModel getAll() {
        return new ApiResponseModel(true, "Ok", blockRepository.findAll());
    }
    public ResBlock getResBlock(Block block){
        ResBlock resBlock=new ResBlock();
        resBlock.setId(block.getId());
        resBlock.setLevel(block.getLevel());
        resBlock.setNameRu(block.getNameRu());
        resBlock.setNameUz(block.getNameUz());
        return resBlock;
    }

    public ApiResponseModel getBlockByLevel(Level level) {
        return new ApiResponseModel(true, "Ok", blockRepository.findAllByLevel(level).stream().map(this::getResBlock).collect(Collectors.toList()));
    }
}


