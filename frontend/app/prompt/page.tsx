import styles from './prompt.module.css';

export default async function NewPrompt() {
  return (
    <div className={styles.container}>
      <h1 className={styles.heading}>
        <span>Describe</span> where you want to go,
      </h1>
      <p className={styles.paragraph}>And our AI Search will help you!</p>
    </div>
  );
}
