import styles from './search.module.css';
import Image from 'next/image';
import User from '@/public/avatars/avatar-1.svg';
import { Button } from '@/components/ui/button';
import Arrow from '@/public/down-arrow.svg';

export default function Search() {
    return (
        <section className={styles.search}>
          <div className={styles.contentLine}>
            <div className={styles.userSection}>
              <Image src={User} alt='User' className={styles.userAvatar} />
              <span className={styles.greeting}>Hi, Username!</span>
            </div>

            <div className={styles.action}>
              <Button className={styles.btnName}>Edit username</Button>
              <Button className={styles.btnAvatar}>Edit avatar</Button>
              <Button className={styles.btnLocation}>
                  Lviv
                  <Image src={Arrow} alt={'Arrow'} className={styles.arrow} />
              </Button>
            </div>
          </div>
        </section>
    );

}