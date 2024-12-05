package com.tokorokoshi.tokoro.modules.promptHistory;

import com.tokorokoshi.tokoro.database.PromptHistory;
import com.tokorokoshi.tokoro.modules.promptHistory.dto.CreateUpdatePromptHistoryDto;
import com.tokorokoshi.tokoro.modules.promptHistory.dto.PromptHistoryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromptHistoryMapper {

    PromptHistory toPromptHistorySchema(CreateUpdatePromptHistoryDto createUpdatePromptHistoryDto);

    PromptHistoryDto toPromptHistoryDto(PromptHistory promptHistory);

    List<PromptHistoryDto> toPromptHistoryDto(List<PromptHistory> promptHistories);

    PromptHistory toPromptHistorySchema(PromptHistoryDto promptHistoryDto);


}
