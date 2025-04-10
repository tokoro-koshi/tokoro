'use client';

import { values } from '@/lib/constants/about/values';
import styles from './about.module.css';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { team } from '@/lib/constants/about/team';
import Image from 'next/image';
import { Button } from '@/components/ui/button';
import routes from '@/lib/constants/routes';
import { useRouter } from 'next/navigation';

export default function AboutComponent() {
  const router = useRouter();

  return (
    <>
      <div className={styles.titleSection}>
        <div className={styles.line}></div>
        <h2 className={styles.title}>About Us</h2>
        <div className={styles.line}></div>
      </div>

      <div className={styles.missionSection}>
        <h2 className={styles.missionTitle}>Our Mission</h2>
        <p className={styles.missionText}>
          At Tokoro, we&apos;re dedicated to creating innovative solutions that
          empower businesses and individuals to achieve their goals. Our mission
          is to provide cutting-edge technology that simplifies complex
          processes and enhances productivity.
        </p>
        <p className={styles.missionText}>
          Founded in 2020, we&apos;ve grown from a small startup to a trusted
          partner for businesses across various industries. Our commitment to
          excellence and customer satisfaction drives everything we do.
        </p>
      </div>

      <div className={styles.storySection}>
        <div className={styles.storyDescription}>
          <h2 className={styles.storyTitle}>Our Story</h2>
          <p className={styles.storyText}>
            Tokoro began with a simple idea: technology should work for people,
            not the other way around. Our founders recognized the need for
            intuitive, powerful solutions that address real-world challenges.
          </p>
          <p className={styles.storyText}>
            Today, we continue to innovate and expand our offerings, guided by
            our core values of integrity, innovation, and exceptional service.
            We believe in building lasting relationships with our clients and
            delivering solutions that exceed expectations.
          </p>
        </div>
        <Image
          className={styles.storyImage}
          src={'/our-story.png'}
          alt={'Story'}
          width={448}
          height={471}
        />
      </div>

      <h2 className={styles.valuesTitle}>Our Values</h2>
      <div className={styles.valuesSection}>
        {values.map((val, index) => (
          <Card key={index} className={styles.valueCard}>
            <CardHeader>
              <CardTitle className={styles.cardTitle}>{val.title}</CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription className={styles.cardDescription}>
                {val.description}
              </CardDescription>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className={styles.teamSection}>
        <h2 className={styles.teamTitle}>Our Team</h2>
        <div className={styles.teamGrid}>
          {team.map((member, index) => (
            <div key={index} className={'justify-items-center'}>
              <Image
                src={member.image}
                alt={member.name}
                className={styles.avatar}
                width={180}
                height={180}
              />
              <h3 className={styles.memberName}>{member.name}</h3>
              <p className={styles.memberRole}>{member.position}</p>
            </div>
          ))}
        </div>
      </div>

      <div className={styles.bottomSection}>
        <h2 className={styles.bottomText}>Still have questions? Ask us!</h2>
        <Button
          className={styles.contactButton}
          onClick={() => router.push(routes.contactUs)}
        >
          Contact Us
        </Button>
      </div>
    </>
  );
}
