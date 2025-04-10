import createMDX from '@next/mdx';

const nextConfig = {
  // Configure `pageExtensions` to include markdown and MDX files
  pageExtensions: ['js', 'jsx', 'md', 'mdx', 'ts', 'tsx'],
  // Enable our S3 bucket support for next/image
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname:
          'tokoro.a4c55fc25483af7392a0ea7c5c114bfc.r2.cloudflarestorage.com',
        pathname: '/**',
      },
    ],
  },
};

const withMDX = createMDX();
export default withMDX(nextConfig);
