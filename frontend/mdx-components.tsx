import type { MDXComponents } from 'mdx/types';

export function useMDXComponents(components: MDXComponents): MDXComponents {
  return {
    ...components,
    h1: (props) => (
      <h1
        className={`mb-6 text-center text-3xl font-bold text-background`}
        {...props}
      />
    ),
    h2: (props) => (
      <h2
        className={`mb-4 mt-8 text-2xl font-semibold text-background`}
        {...props}
      />
    ),
    h3: (props) => (
      <h3
        className={`mb-3 mt-6 text-xl font-medium text-background`}
        {...props}
      />
    ),
    p: (props) => (
      <p
        className={`text-base leading-relaxed text-background`}
        {...props}
      />
    ),
    ul: (props) => (
      <ul className={`mb-4 list-disc pl-6 text-background`} {...props} />
    ),
    ol: (props) => (
      <ol className={`mb-4 list-decimal pl-6 text-background`} {...props} />
    ),
    li: (props) => <li className={`mb-2 text-background`} {...props} />,
    blockquote: (props) => (
      <blockquote
        className={`mb-4 border-l-4 border-gray-300 pl-4 italic text-background`}
        {...props}
      />
    ),
    code: (props) => (
      <code
        className={`rounded-md bg-gray-100 px-2 py-1 font-mono text-sm text-background`}
        {...props}
      />
    ),
    inlineCode: (props) => (
      <code
        className={`rounded-md bg-gray-100 px-2 py-1 font-mono text-sm text-background`}
        {...props}
      />
    ),
    hr: (props) => (
      <hr className={'my-8 border-t border-gray-300'} {...props} />
    ),
    a: (props) => <a className={`text-blue-300 hover:underline`} {...props} />,
    table: (props) => (
      <table className={'mb-4 w-full table-auto border-collapse'} {...props} />
    ),
    thead: (props) => <thead className={'bg-gray-100'} {...props} />,
    th: (props) => (
      <th
        className={`border border-gray-300 px-4 py-2 text-left font-semibold text-background`}
        {...props}
      />
    ),
    tbody: (props) => <tbody {...props} />,
    tr: (props) => <tr className={'even:bg-gray-50'} {...props} />,
    td: (props) => (
      <td
        className={`border border-gray-300 px-4 py-2 text-background`}
        {...props}
      />
    ),
    // eslint-disable-next-line @next/next/no-img-element
    img: (props) => (
      <img
        className={'mb-4 max-w-full rounded-md object-cover'}
        {...props}
        alt={'Article image'}
      />
    ),
  };
}
