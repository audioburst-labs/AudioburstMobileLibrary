Pod::Spec.new do |spec|
    spec.name                     = 'AudioburstMobileLibrary'
    spec.version                  = '0.0.29'
    spec.homepage                 = 'https://github.com/audioburst-labs/AudioburstMobileLibrary'
    spec.source                   = { :git => "https://github.com/audioburst-labs/AudioburstMobileLibrary.git", :tag => spec.version.to_s }
    spec.author                   = { 'Audioburst' => 'kamil@audioburst.com' }
    spec.license                  = { :type => 'Custom'}
    spec.summary                  = 'AudioburstMobileLibrary is a multi platform library that allows convenient access to the Audioburst’s Content APIs.'
    spec.source_files             = 'AudioburstMobileLibrary.xcframework/*/AudioburstMobileLibrary.framework/Headers/*.{h,m,swift}'

    spec.platform                 = :ios
    spec.vendored_frameworks      = "AudioburstMobileLibrary.xcframework"
    spec.ios.deployment_target    = '12.0'
    spec.swift_version            = "5.0"
    spec.pod_target_xcconfig      = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
    spec.user_target_xcconfig     = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
end