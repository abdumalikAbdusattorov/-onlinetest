package uz.pdp.apponlinetestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.pdp.apponlinetestserver.entity.Block;
import uz.pdp.apponlinetestserver.entity.Test;
import uz.pdp.apponlinetestserver.entity.TestBlock;
import uz.pdp.apponlinetestserver.entity.TestWithScore;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.exception.ResourceNotFoundException;
import uz.pdp.apponlinetestserver.payload.*;
import uz.pdp.apponlinetestserver.repository.BlockRepository;
import uz.pdp.apponlinetestserver.repository.TestBlockRepositrory;
import uz.pdp.apponlinetestserver.repository.TestRepository;
import uz.pdp.apponlinetestserver.repository.TestWithScoreRepository;
import uz.pdp.apponlinetestserver.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class TestBlockService {

    @Autowired
    TestBlockRepositrory testBlockRepositrory;

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestWithScoreRepository testWithScoreRepository;

    @Autowired
    BlockService blockService;

    @Autowired
    TestService testService;

    public ApiResponse saveOrEdit(ReqTestBlock reqTestBlock) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setMessage("Saved");
            apiResponse.setSuccess(true);
            TestBlock testBlock = new TestBlock();

            if (reqTestBlock.getId() != null) {
//                testBlock = testBlockRepositrory.findById(reqTestBlock.getId()).orElseThrow(() -> new
//                        ResourceNotFoundException("testBlock", "id", reqTestBlock.getId()));
                apiResponse.setMessage("Edited");
                testBlockRepositrory.deleteById(reqTestBlock.getId());
            }

            testBlock.setBlock(blockRepository.findById(reqTestBlock.getBlockId()).orElseThrow(() -> new
                    ResourceNotFoundException("block", "id", reqTestBlock.getBlockId())));
            TestBlock savedTestBlock = testBlockRepositrory.save(testBlock);
            for (ReqTestWithScore reqTestWithScore : reqTestBlock.getReqTestWithScores()) {
                TestWithScore testWithScore = new TestWithScore();
                testWithScore.setTestBlock(savedTestBlock);
                testWithScore.setScore(reqTestWithScore.getScore());
                testWithScore.setTest(testRepository.findById(reqTestWithScore.getTestId()).orElseThrow(() -> new
                        ResourceNotFoundException("test", "id", reqTestWithScore.getTestId())));
                testWithScoreRepository.save(testWithScore);
            }
        } catch (Exception e) {
            apiResponse.setMessage("Error");
            apiResponse.setSuccess(false);
        }
        return apiResponse;
    }


    public ResTestBlock getResTestBlock(TestBlock testBlock) {
        ResTestBlock resTestBlock = new ResTestBlock();
        resTestBlock.setId(testBlock.getId());
        resTestBlock.setResBlock(blockService.getResBlock(testBlock.getBlock()));
        List<Block> blocksByLevel = blockRepository.findAllByLevel(testBlock.getBlock().getLevel());
        List<ResBlock> resBlockList = new ArrayList<>();
        for (Block block : blocksByLevel) {
            resBlockList.add(blockService.getResBlock(block));
        }
        resTestBlock.setResBlockListByLevel(resBlockList);
        List<ResTestWithScore> resTestWithScores = new ArrayList<>();
        for (TestWithScore testWithScore : testBlock.getTestWithScores()) {

            resTestWithScores.add(getResTestWithScore(testWithScore));
        }
        resTestBlock.setResTestWithScoreList(resTestWithScores);
        return resTestBlock;
    }

    public ResTestWithScore getResTestWithScore(TestWithScore testWithScore) {
        ResTestWithScore resTestWithScore = new ResTestWithScore();
        resTestWithScore.setId(testWithScore.getId());
        resTestWithScore.setResTest(testService.getResTest(testWithScore.getTest()));
        List<ResTest> resTestListBySubjectAndLevel = new ArrayList<>();
        List<Test> testList = testRepository.findAllBySubjectIdAndLevel(testWithScore.getTest().getSubject().getId(), testWithScore.getTest().getLevel());
        for (Test test : testList) {
            resTestListBySubjectAndLevel.add(testService.getResTest(test));
        }
        resTestWithScore.setResTestListBySubjectAndLevel(resTestListBySubjectAndLevel);
        resTestWithScore.setScore(testWithScore.getScore());
        return resTestWithScore;
    }

//    public ApiResponseModel getAllByBlock(Integer blockId) {
////        List<TestBlock> testBlockList = testBlockRepositrory.findAllByBlockId(blockId);
////        List<ResTestBlock> resTestBlockList=new ArrayList<>();
////        for (TestBlock testBlock : testBlockList) {
////            resTestBlockList.add(getResTestBlock(testBlock));
////        }
////        return new ApiResponseModel(true,"Ok",resTestBlockList);
//////        return new ApiResponseModel(true,"Ok",testBlockList.stream().map(this::getResTestBlock).collect(Collectors.toList()));
////    }

    public ApiResponseModel getByLevelAndBlock(Level level, Integer blockId) {
        List<TestBlock> testBlockList = testBlockRepositrory.findAllByBlockIdAndBlockLevel(blockId, level);
        List<ResTestBlock> resTestBlockList = new ArrayList<>();
        for (TestBlock testBlock : testBlockList) {
            resTestBlockList.add(getResTestBlock(testBlock));
        }
        return new ApiResponseModel(true, "OK", resTestBlockList);
    }

    public ResTestBlock getById(UUID id) {
        Optional<TestBlock> optionalTestBlock = testBlockRepositrory.findById(id);
        if (optionalTestBlock.isPresent()) {
            TestBlock testBlock = optionalTestBlock.get();
            return getResTestBlock(testBlock);
        }
        return null;
    }

    public ResPageable getTestBlockByPageable(Integer page, Integer size) {
        Page<TestBlock> testBlocks = testBlockRepositrory.findAll(CommonUtils.getPageableById(page, size));
        return new ResPageable(testBlocks.stream().map(this::getResTestBlock).collect(Collectors.toList()), testBlocks.getTotalElements(), page);
    }

    public ApiResponse deleteTestBlock(UUID id) {
        try {
            testBlockRepositrory.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);

        }

    }

    public ApiResponseModel getAll() {
        return new ApiResponseModel(true, "OK",
                testBlockRepositrory.findAll().stream().map(this::getResTestBlock).collect(Collectors.toList()));
    }
}
