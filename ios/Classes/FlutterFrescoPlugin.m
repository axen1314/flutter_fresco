#import "FlutterFrescoPlugin.h"
#if __has_include(<flutter_fresco/flutter_fresco-Swift.h>)
#import <flutter_fresco/flutter_fresco-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_fresco-Swift.h"
#endif

@implementation FlutterFrescoPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterFrescoPlugin registerWithRegistrar:registrar];
}
@end
