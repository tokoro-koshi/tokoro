import Search from "@/components/fyp/search/search";
import CarouselSection from "@/components/fyp/carousel-section/carousel-section";
import { places } from '@/lib/constants/fyp/places';

export default async function Fyp() {
    return (
        <>
            <Search/>
            <CarouselSection title="Saved Places" places={places}/>
            <CarouselSection title="Search History" places={places}/>
            <CarouselSection title="Recommended Places" places={places}/>
        </>
    );
}