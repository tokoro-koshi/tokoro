import type { Config } from 'tailwindcss';
import tailwindcss_animate from 'tailwindcss-animate';

const variants = ['sm', 'md', 'lg', 'xl', '2xl'];

const config: Config = {
  darkMode: ['class'],
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    './mdx-components.tsx',
  ],
  safelist: [
    {
      pattern: /^grid-cols-(\d+|auto)$/,
      variants,
    },
  ],
  theme: {
    extend: {
      colors: {
        background: 'hsl(var(--background), <alpha-value>)',
        foreground: 'hsl(var(--foreground), <alpha-value>)',
        card: {
          DEFAULT: 'hsl(var(--card), <alpha-value>)',
          foreground: 'hsl(var(--card-foreground), <alpha-value>)',
        },
        popover: {
          DEFAULT: 'hsl(var(--popover), <alpha-value>)',
          foreground: 'hsl(var(--popover-foreground), <alpha-value>)',
        },
        primary: {
          DEFAULT: 'hsl(var(--primary), <alpha-value>)',
          foreground: 'hsl(var(--primary-foreground), <alpha-value>)',
        },
        secondary: {
          DEFAULT: 'hsl(var(--secondary), <alpha-value>)',
          foreground: 'hsl(var(--secondary-foreground), <alpha-value>)',
        },
        muted: {
          DEFAULT: 'hsl(var(--muted), <alpha-value>)',
          foreground: 'hsl(var(--muted-foreground), <alpha-value>)',
        },
        accent: {
          DEFAULT: 'hsl(var(--accent), <alpha-value>)',
          foreground: 'hsl(var(--accent-foreground), <alpha-value>)',
        },
        destructive: {
          DEFAULT: 'hsl(var(--destructive), <alpha-value>)',
          foreground: 'hsl(var(--destructive-foreground), <alpha-value>)',
        },
        sidebar: {
          DEFAULT: 'hsl(var(--sidebar-background))',
          foreground: 'hsl(var(--sidebar-foreground), <alpha-value>)',
          primary: 'hsl(var(--sidebar-primary))',
          'primary-foreground': 'hsl(var(--sidebar-primary-foreground))',
          accent: 'hsl(var(--sidebar-accent))',
          'accent-foreground': 'hsl(var(--sidebar-accent-foreground))',
          border: 'hsl(var(--sidebar-border))',
          ring: 'hsl(var(--sidebar-ring))',
        },
        border: 'hsl(var(--border), <alpha-value>)',
        input: 'hsl(var(--input), <alpha-value>)',
        ring: 'hsl(var(--ring), <alpha-value>)',
        chart: {
          '1': 'hsl(var(--chart-1), <alpha-value>)',
          '2': 'hsl(var(--chart-2), <alpha-value>)',
          '3': 'hsl(var(--chart-3), <alpha-value>)',
          '4': 'hsl(var(--chart-4), <alpha-value>)',
          '5': 'hsl(var(--chart-5), <alpha-value>)',
        },
      },
      borderRadius: {
        lg: 'var(--radius)',
        md: 'calc(var(--radius) - 2px)',
        sm: 'calc(var(--radius) - 4px)',
      },
      fontFamily: {
        base: ['var(--font-raleway)', 'Arial', 'Helvetica', 'sans-serif'],
        mono: ['var(--font-jetbrains-mono)', 'monospace'],
      },
      backgroundImage: {
        'custom-gradient':
          'linear-gradient(to bottom, #8181DE00 0%, #0F0F3194 58%, #0F0F31 100%)',
      },
    },
  },
  plugins: [tailwindcss_animate],
};
export default config;
