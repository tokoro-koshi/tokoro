import  styles from './blog.module.css';
import {Search, Send} from "lucide-react";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {places} from "@/lib/constants/blog/places";
import Image from "next/image";
import SaveButton from "@/components/cards/items/save/save";

export default function Blog() {
    return (
        <div className={styles.blog}>
            <div className={styles.placeSection}>

                <div className={styles.inputArea}>
                    <div className={styles.inputContainer}>
                        <div className={styles.inputIcon}>
                            <Search className='h-5 w-5'/>
                        </div>
                        <Input
                            placeholder='Type command or search...'
                            className={styles.inputField}
                        />
                        <Button
                            size='icon'
                            className={styles.sendButton}
                        >
                            <Send className='h-5 w-5'/>
                        </Button>
                    </div>
                </div>

                <div className={styles.topPlaces}>
                    <h2 className={styles.header}>TOP 5 PLACES OF THE WEEK</h2>
                    <div className={styles.places}>
                        {places.map((place, index) => (
                            <div key={place.id} className={styles.placeItem}>
                                <div className={styles.placeNumber}>
                                    {index + 1}.
                                </div>
                                <div className={styles.placeImage}>
                                    <Image
                                        src={place.pictures[1] || '/placeholder.svg'}
                                        alt={place.name}
                                        width={91}
                                        height={91}
                                        className={styles.image}
                                    />
                                </div>
                                <div className={styles.placeDetails}>
                                    <h3 className={styles.placeName}>
                                        {place.name}
                                    </h3>

                                    <p className={styles.placeLocation}>
                                        {place.location.city}, {place.location.address}
                                    </p>
                                </div>
                                <SaveButton
                                    className={styles.saveButton}
                                    placeId={place.id}
                                    variant={'light'}
                                />
                            </div>
                        ))}
                    </div>
                </div>

            </div>
            {/*<div className="col-span-2 w-[970px] h-28 bg-black"></div>*/}
        </div>
    );
}