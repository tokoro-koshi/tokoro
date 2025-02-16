package com.tokorokoshi.tokoro.modules.testimonials;

import com.tokorokoshi.tokoro.database.Testimonial;
import com.tokorokoshi.tokoro.modules.testimonials.dto.CreateUpdateTestimonialDto;
import com.tokorokoshi.tokoro.modules.testimonials.dto.TestimonialDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestimonialMapper {
    Testimonial toTestimonialSchema(CreateUpdateTestimonialDto testimonialDto);

    TestimonialDto toTestimonialDto(Testimonial testimonial);

    List<TestimonialDto> toTestimonialDto(List<Testimonial> testimonials);
}
